package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.PageRequest;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilEstadoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilPersonaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilPersonaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PerfilEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.EstadosEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PerfilEstadoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PerfilPersonaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaNaturalGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.EstadosRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PerfilEstadoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PerfilRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PerfilPersonaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.PerfilService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements PerfilService {

    private final PerfilRepository perfilRepository;

    private final PerfilEstadoRepository perfilEstadoRepository;

    private final EstadosRepository estadosRepository;

    private final PerfilPersonaRepository perfilPersonaRepository;

    private final PersonaGeneralRepository personaGeneralRepository;

    private final EntityManager entityManager;

    private static final String SI = "1";

    private static final String NO = "0";

    /*
     * LISTAR PERFILES
     */

    @Override
    @Transactional(readOnly = true)
    public List<PerfilDTO> listProfiles() {

        return perfilRepository.findAll(
                Sort.by(Sort.Direction.ASC, "id")
        ).stream()
                .map(this::mapToDTO)
                .toList();
    }

    /*
     * CONSULTAR PERFIL POR ID
     */

    @Override
    @Transactional(readOnly = true)
    public PerfilDTO getProfileById(Long id) {

        return mapToDTO(findProfile(id));
    }

    /*
    * LISTAR ESTADOS ASOCIADOS AL PERFIL
    */

    @Override
    @Transactional(readOnly = true)
    public List<EstadoDTO> listProfileStates(Long idPerfil) {

        findProfile(idPerfil);

        List<Long> estadosIds = perfilEstadoRepository
                .findAllByIdPerfil(idPerfil)
                .stream()
                .filter(relacion -> SI.equals(relacion.getEstado()))
                .map(PerfilEstadoEntity::getIdEstado)
                .distinct()
                .toList();

        List<EstadosEntity> estados =
                estadosRepository.findAllById(estadosIds);

        if (estados.size() != estadosIds.size()) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El perfil contiene relaciones con estados "
                            + "que no existen."
            );
        }

        return estados.stream()
                .sorted(
                        java.util.Comparator.comparing(
                                EstadosEntity::getId
                        )
                )
                .map(this::mapEstadoToDTO)
                .toList();
    }

    /*
    * GUARDAR ESTADOS ASOCIADOS AL PERFIL
    */

    @Override
    @Transactional
    public void saveProfileStates(
            Long idPerfil,
            PerfilEstadoFormularioDTO dto
    ) {

        if (dto == null || dto.estadosIds() == null) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Debe proporcionar la lista de estados."
            );
        }

        if (dto.estadosIds().stream()
                .anyMatch(id -> id == null || id <= 0)) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Existen identificadores de estados no válidos."
            );
        }

        findProfileForUpdate(idPerfil);

        Set<Long> estadosSeleccionados =
                new LinkedHashSet<>(dto.estadosIds());

        for (Long idEstado : estadosSeleccionados.stream()
                .sorted()
                .toList()) {

            estadosRepository.findByIdForUpdate(idEstado)
                    .orElseThrow(() -> new ApiException(
                            HttpStatus.NOT_FOUND,
                            "El estado con identificador "
                                    + idEstado
                                    + " no existe."
                    ));
        }

        List<PerfilEstadoEntity> relacionesActuales =
                perfilEstadoRepository
                        .findAllByIdPerfil(idPerfil)
                        .stream()
                        .sorted(
                                java.util.Comparator.comparing(
                                        PerfilEstadoEntity::getId
                                )
                        )
                        .toList();

        Set<Long> estadosConservados = new HashSet<>();

        List<PerfilEstadoEntity> relacionesEliminar =
                new ArrayList<>();

        List<PerfilEstadoEntity> relacionesActualizar =
                new ArrayList<>();

        for (PerfilEstadoEntity relacion : relacionesActuales) {

            Long idEstado = relacion.getIdEstado();

            if (!estadosSeleccionados.contains(idEstado)) {

                relacionesEliminar.add(relacion);

                continue;
            }

            if (!estadosConservados.add(idEstado)) {

                relacionesEliminar.add(relacion);

                continue;
            }

            if (!SI.equals(relacion.getEstado())) {

                relacion.setEstado(SI);

                relacion.setRegistradoPor(
                        RegistradoPorUtils.value(Accion.UPDATE)
                );

                relacion.setFechaCambio(new Date());

                relacionesActualizar.add(relacion);
            }
        }

        for (PerfilEstadoEntity relacion : relacionesEliminar) {

            executeDeleteProfileStateProcedure(
                    relacion.getId()
            );

            entityManager.detach(relacion);
        }

        if (!relacionesActualizar.isEmpty()) {

            perfilEstadoRepository.saveAll(
                    relacionesActualizar
            );
        }

        List<PerfilEstadoEntity> relacionesCrear =
                new ArrayList<>();

        for (Long idEstado : estadosSeleccionados) {

            if (estadosConservados.contains(idEstado)) {

                continue;
            }

            PerfilEstadoEntity relacion =
                    new PerfilEstadoEntity();

            relacion.setIdPerfil(idPerfil);

            relacion.setIdEstado(idEstado);

            relacion.setEstado(SI);

            relacion.setRegistradoPor(
                    RegistradoPorUtils.value(Accion.INSERT)
            );

            relacion.setFechaCambio(new Date());

            relacionesCrear.add(relacion);
        }

        if (!relacionesCrear.isEmpty()) {

            perfilEstadoRepository.saveAll(
                    relacionesCrear
            );
        }

        perfilEstadoRepository.flush();
    }

    /*
     * CREAR PERFIL
     */

    @Override
    @Transactional
    public void saveProfile(PerfilFormularioDTO dto) {

        validateForm(dto);

        PerfilEntity entity = new PerfilEntity();

        applyForm(entity, dto);

        entity.setRegistradoPor(
                RegistradoPorUtils.value(Accion.INSERT)
        );

        entity.setFechaCambio(new Date());

        perfilRepository.saveAndFlush(entity);
    }

    /*
     * ACTUALIZAR PERFIL
     */

    @Override
    @Transactional
    public void updateProfile(
            Long id,
            PerfilFormularioDTO dto
    ) {

        validateForm(dto);

        PerfilEntity entity = findProfileForUpdate(id);

        applyForm(entity, dto);

        entity.setRegistradoPor(
                RegistradoPorUtils.value(Accion.UPDATE)
        );

        entity.setFechaCambio(new Date());

        perfilRepository.saveAndFlush(entity);
    }

    /*
     * ELIMINAR PERFIL
     */

    @Override
    @Transactional
    public void deleteProfile(Long id) {

        PerfilEntity entity = findProfileForUpdate(id);

        validateDeletion(entity.getId());

        executeDeleteProcedure(entity.getId());

        entityManager.clear();
    }

    /*
     * ELIMINAR MULTIPLES PERFILES
     */

    @Override
    @Transactional
    public void deleteProfiles(List<Long> ids) {

        if (ids == null
                || ids.isEmpty()
                || ids.stream().anyMatch(
                        id -> id == null || id <= 0
                )) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Debe seleccionar perfiles válidos."
            );
        }

        /*
         * Eliminar identificadores duplicados.
         * Ordenar los identificadores para adquirir
         * los bloqueos siempre en el mismo orden.
         */

        List<Long> uniqueIds = ids.stream()
                .distinct()
                .sorted()
                .toList();

        /*
         * Validar y bloquear todos los perfiles
         * antes de ejecutar cualquier eliminación.
         */

        for (Long id : uniqueIds) {

            findProfileForUpdate(id);

            validateDeletion(id);
        }

        /*
         * Ejecutar las eliminaciones mediante
         * el procedimiento almacenado.
         */

        for (Long id : uniqueIds) {

            executeDeleteProcedure(id);
        }

        entityManager.clear();
    }

    /*
     * BUSCAR PERFIL
     */

    private PerfilEntity findProfile(Long id) {

        validateId(id);

        return perfilRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Perfil no encontrado."
                ));
    }

    /*
     * BUSCAR Y BLOQUEAR PERFIL
     */

    private PerfilEntity findProfileForUpdate(Long id) {

        validateId(id);

        return perfilRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Perfil no encontrado."
                ));
    }

    /*
     * VALIDAR IDENTIFICADOR
     */

    private void validateId(Long id) {

        if (id == null || id <= 0) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El identificador del perfil no es válido."
            );
        }
    }

    /*
     * APLICAR DATOS DEL FORMULARIO
     */

    private void applyForm(
            PerfilEntity entity,
            PerfilFormularioDTO dto
    ) {

        entity.setNombre(
                dto.nombre().trim()
        );

        entity.setDescripcion(
                dto.descripcion().trim()
        );

        entity.setProceso(
                normalizeOptional(dto.proceso())
        );

        entity.setEsProceso(
                dto.esProceso().trim()
        );

        entity.setCodigo(
                normalizeOptional(dto.codigo())
        );
    }

    /*
     * VALIDAR FORMULARIO
     */

    private void validateForm(
            PerfilFormularioDTO dto
    ) {

        if (dto == null) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La información del perfil es obligatoria."
            );
        }

        validateRequiredText(
                dto.nombre(),
                "El nombre del perfil",
                150
        );

        validateRequiredText(
                dto.descripcion(),
                "La descripción del perfil",
                150
        );

        validateOptionalLength(
                dto.proceso(),
                "El proceso",
                500
        );

        validateOptionalLength(
                dto.codigo(),
                "El código del perfil",
                30
        );

        if (!StringUtils.hasText(dto.esProceso())) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicar si el perfil es un proceso."
            );
        }

        String esProceso = dto.esProceso().trim();

        if (!SI.equals(esProceso)
                && !NO.equals(esProceso)) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El indicador de proceso debe ser 1 o 0."
            );
        }
    }

    /*
     * VALIDAR TEXTO OBLIGATORIO
     */

    private void validateRequiredText(
            String value,
            String field,
            int maxLength
    ) {

        if (!StringUtils.hasText(value)) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    field + " es obligatorio."
            );
        }

        if (value.trim().length() > maxLength) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    field + " no puede superar "
                            + maxLength + " caracteres."
            );
        }
    }

    /*
     * VALIDAR LONGITUD DE CAMPO OPCIONAL
     */

    private void validateOptionalLength(
            String value,
            String field,
            int maxLength
    ) {

        if (StringUtils.hasText(value)
                && value.trim().length() > maxLength) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    field + " no puede superar "
                            + maxLength + " caracteres."
            );
        }
    }

    /*
     * NORMALIZAR CAMPOS OPCIONALES
     */

    private String normalizeOptional(String value) {

        return StringUtils.hasText(value)
                ? value.trim()
                : null;
    }

    /*
     * VALIDAR DEPENDENCIAS DEL PERFIL
     */

    private void validateDeletion(Long id) {

        boolean hasStates = hasReferences(
                """
                SELECT COUNT(p)
                FROM PerfilEstadoEntity p
                WHERE p.idPerfil = :id
                """,
                id
        );

        if (hasStates) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el perfil porque "
                            + "tiene estados asociados."
            );
        }

        boolean hasPersons = hasReferences(
                """
                SELECT COUNT(p)
                FROM PerfilPersonaEntity p
                WHERE p.idPerfil = :id
                """,
                id
        );

        if (hasPersons) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el perfil porque "
                            + "tiene personas asociadas."
            );
        }

        boolean hasPermissions = hasReferences(
                """
                SELECT COUNT(p)
                FROM PermisoEntity p
                WHERE p.idPerfil = :id
                """,
                id
        );

        if (hasPermissions) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el perfil porque "
                            + "tiene permisos asociados."
            );
        }
    }

    /*
     * CONSULTAR EXISTENCIA DE RELACIONES
     */

    private boolean hasReferences(
            String query,
            Long id
    ) {

        return entityManager
                .createQuery(query, Long.class)
                .setParameter("id", id)
                .getSingleResult() > 0;
    }

    /*
     * EJECUTAR PROCEDIMIENTO DE ELIMINACION
     */

    private void executeDeleteProcedure(Long id) {

        StoredProcedureQuery procedure =
                entityManager.createStoredProcedureQuery(
                        "RVDIES.PR_RVD_D_PERFILES"
                );

        procedure.registerStoredProcedureParameter(
                1,
                Long.class,
                ParameterMode.IN
        );

        procedure.registerStoredProcedureParameter(
                2,
                String.class,
                ParameterMode.IN
        );

        procedure.registerStoredProcedureParameter(
                3,
                BigDecimal.class,
                ParameterMode.OUT
        );

        procedure.setParameter(
                1,
                id
        );

        procedure.setParameter(
                2,
                RegistradoPorUtils.value(Accion.DELETE)
        );

        procedure.execute();

        Object result =
                procedure.getOutputParameterValue(3);

        if (!(result instanceof Number number)
                || number.intValue() != 1) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No fue posible eliminar el perfil."
            );
        }
    }

    /*
     * CONVERTIR ENTIDAD A DTO
     */

    private PerfilDTO mapToDTO(
            PerfilEntity entity
    ) {

        return new PerfilDTO(

                entity.getId(),

                entity.getNombre(),

                entity.getDescripcion(),

                entity.getProceso(),

                entity.getEsProceso(),

                entity.getCodigo()
        );
    }


    /*
    * ELIMINAR RELACION PERFIL-ESTADO
    * MEDIANTE PROCEDIMIENTO ALMACENADO
    */

    private void executeDeleteProfileStateProcedure(
            Long idRelacion
    ) {

        StoredProcedureQuery procedure =
                entityManager.createStoredProcedureQuery(
                        "RVDIES.PR_RVD_D_PERFILESTADO"
                );

        procedure.registerStoredProcedureParameter(
                1,
                Long.class,
                ParameterMode.IN
        );

        procedure.registerStoredProcedureParameter(
                2,
                String.class,
                ParameterMode.IN
        );

        procedure.registerStoredProcedureParameter(
                3,
                BigDecimal.class,
                ParameterMode.OUT
        );

        procedure.setParameter(
                1,
                idRelacion
        );

        procedure.setParameter(
                2,
                RegistradoPorUtils.value(Accion.DELETE)
        );

        procedure.execute();

        Object result =
                procedure.getOutputParameterValue(3);

        if (!(result instanceof Number number)
                || number.intValue() != 1) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No fue posible eliminar la relación "
                            + "entre el perfil y el estado."
            );
        }
    }

    /*
    * CONVERTIR ESTADO A DTO
    */

    private EstadoDTO mapEstadoToDTO(
            EstadosEntity entity
    ) {

        return new EstadoDTO(

                entity.getId(),

                entity.getNombre(),

                entity.getDescripcion(),

                entity.getTipo(),

                entity.getOrden()
        );
    }

    /*
    * BUSCAR PERSONAS PARA ASOCIAR A PERFILES
    */

    @Override
    @Transactional(readOnly = true)
    public List<PerfilPersonaDTO> searchPersons(String term) {

        if (term == null) {

            return List.of();
        }

        String search = term.trim();

        if (search.length() < 2) {

            return List.of();
        }

        if (search.length() > 150) {

            throw new ApiException(

                    HttpStatus.BAD_REQUEST,

                    "El criterio de búsqueda no puede superar "
                            + "los 150 caracteres."

            );
        }

        if (search.contains("%") || search.contains("_")) {

            throw new ApiException(

                    HttpStatus.BAD_REQUEST,

                    "El criterio de búsqueda contiene "
                            + "caracteres no permitidos."

            );
        }

        return personaGeneralRepository
                .searchPersonsForProfiles(

                        search,

                        PageRequest.of(0, 20)

                )
                .stream()
                .map(this::mapPersonaToDTO)
                .toList();
    }

    /*
    * LISTAR PERSONAS ASOCIADAS AL PERFIL
    */

    @Override
    @Transactional(readOnly = true)
    public List<PerfilPersonaDTO> listProfilePersons(
            Long idPerfil
    ) {

        // Verificar existencia del perfil.

        findProfile(idPerfil);

        // Obtener las relaciones activas.

        List<Long> personasIds = perfilPersonaRepository

                .findAllByIdPerfil(idPerfil)

                .stream()

                .filter(
                        relacion -> SI.equals(
                                relacion.getEstado()
                        )
                )

                .map(
                        PerfilPersonaEntity::getIdPersonaGeneral
                )

                .distinct()

                .toList();

        // Evitar consultas con una lista vacía.

        if (personasIds.isEmpty()) {

            return List.of();
        }

        List<PersonaGeneralEntity> personas =
                findPersonsByIdsInBatches(personasIds);

        // Verificar posibles relaciones huérfanas.

        if (personas.size() != personasIds.size()) {

            throw new ApiException(

                    HttpStatus.CONFLICT,

                    "El perfil contiene relaciones con personas "
                            + "que no existen en el catálogo institucional."

            );
        }

        return personas.stream()

                .map(this::mapPersonaToDTO)

                .sorted(
                        java.util.Comparator.comparing(
                                PerfilPersonaDTO::nombreCompleto
                        )
                )

                .toList();
    }

    /*
    * CONSULTAR PERSONAS POR BLOQUES
    */

    private List<PersonaGeneralEntity> findPersonsByIdsInBatches(

            List<Long> ids

    ) {

        if (ids.isEmpty()) {

            return List.of();
        }

        List<PersonaGeneralEntity> result =
                new ArrayList<>();

        final int batchSize = 900;

        for (int i = 0; i < ids.size(); i += batchSize) {

            List<Long> batch = ids.subList(

                    i,

                    Math.min(i + batchSize, ids.size())

            );

            result.addAll(

                    personaGeneralRepository
                            .findPersonsForProfilesByIds(batch)

            );
        }

        return result;
    }


    /*
    * GUARDAR PERSONAS ASOCIADAS AL PERFIL
    */

    @Override
    @Transactional
    public void saveProfilePersons(

            Long idPerfil,

            PerfilPersonaFormularioDTO dto

    ) {

        // Validar la información recibida.

        if (dto == null || dto.personasIds() == null) {

            throw new ApiException(

                    HttpStatus.BAD_REQUEST,

                    "Debe proporcionar la lista de personas."

            );
        }

        if (dto.personasIds().stream()
                .anyMatch(id -> id == null || id <= 0)) {

            throw new ApiException(

                    HttpStatus.BAD_REQUEST,

                    "Existen identificadores de personas no válidos."

            );
        }

        // Verificar y bloquear el perfil.

        findProfileForUpdate(idPerfil);

        // Eliminar duplicados de la selección.

        Set<Long> personasSeleccionadas =
                new LinkedHashSet<>(dto.personasIds());

        // Verificar existencia de las personas seleccionadas.

        List<PersonaGeneralEntity> personasExistentes =
                findPersonsByIdsInBatches(

                        new ArrayList<>(personasSeleccionadas)

                );

        if (personasExistentes.size()
                != personasSeleccionadas.size()) {

            throw new ApiException(

                    HttpStatus.NOT_FOUND,

                    "Una o más personas seleccionadas "
                            + "no existen en el catálogo institucional."

            );
        }

        // Consultar las relaciones actuales.

        List<PerfilPersonaEntity> relacionesActuales =
                perfilPersonaRepository

                        .findAllByIdPerfil(idPerfil)

                        .stream()

                        .sorted(

                                java.util.Comparator.comparing(
                                        PerfilPersonaEntity::getId
                                )

                        )

                        .toList();

        Set<Long> personasConservadas =
                new HashSet<>();

        List<PerfilPersonaEntity> relacionesEliminar =
                new ArrayList<>();

        List<PerfilPersonaEntity> relacionesActualizar =
                new ArrayList<>();

        // Determinar las relaciones que deben conservarse,
        // actualizarse o eliminarse.

        for (PerfilPersonaEntity relacion : relacionesActuales) {

            Long idPersona =
                    relacion.getIdPersonaGeneral();

            // La persona fue retirada de la selección.

            if (!personasSeleccionadas.contains(idPersona)) {

                relacionesEliminar.add(relacion);

                continue;
            }

            // Evitar relaciones duplicadas.

            if (!personasConservadas.add(idPersona)) {

                relacionesEliminar.add(relacion);

                continue;
            }

            // Reactivar una relación existente.

            if (!SI.equals(relacion.getEstado())) {

                relacion.setEstado(SI);

                relacion.setRegistradoPor(

                        RegistradoPorUtils.value(
                                Accion.UPDATE
                        )

                );

                relacion.setFechaCambio(new Date());

                relacionesActualizar.add(relacion);
            }
        }

        // Eliminar las relaciones retiradas.

        for (PerfilPersonaEntity relacion : relacionesEliminar) {

            executeDeleteProfilePersonProcedure(

                    relacion.getId()

            );

            entityManager.detach(relacion);
        }

        // Guardar las relaciones reactivadas.

        if (!relacionesActualizar.isEmpty()) {

            perfilPersonaRepository.saveAll(

                    relacionesActualizar

            );
        }

        // Crear nuevas relaciones.

        List<PerfilPersonaEntity> relacionesCrear =
                new ArrayList<>();

        for (Long idPersona : personasSeleccionadas) {

            if (personasConservadas.contains(idPersona)) {

                continue;
            }

            PerfilPersonaEntity relacion =
                    new PerfilPersonaEntity();

            relacion.setIdPerfil(idPerfil);

            relacion.setIdPersonaGeneral(idPersona);

            relacion.setEstado(SI);

            relacion.setRegistradoPor(

                    RegistradoPorUtils.value(
                            Accion.INSERT
                    )

            );

            relacion.setFechaCambio(new Date());

            relacionesCrear.add(relacion);
        }

        if (!relacionesCrear.isEmpty()) {

            perfilPersonaRepository.saveAll(

                    relacionesCrear

            );
        }

        perfilPersonaRepository.flush();
    }

    /*
    * ELIMINAR RELACION PERFIL-PERSONA
    * MEDIANTE PROCEDIMIENTO ALMACENADO
    */

    private void executeDeleteProfilePersonProcedure(

            Long idRelacion

    ) {

        StoredProcedureQuery procedure =
                entityManager.createStoredProcedureQuery(

                        "RVDIES.PR_RVD_D_PERFILPERSONA"

                );

        procedure.registerStoredProcedureParameter(

                1,

                Long.class,

                ParameterMode.IN

        );

        procedure.registerStoredProcedureParameter(

                2,

                String.class,

                ParameterMode.IN

        );

        procedure.registerStoredProcedureParameter(

                3,

                BigDecimal.class,

                ParameterMode.OUT

        );

        procedure.setParameter(

                1,

                idRelacion

        );

        procedure.setParameter(

                2,

                RegistradoPorUtils.value(
                        Accion.DELETE
                )

        );

        procedure.execute();

        Object result =
                procedure.getOutputParameterValue(3);

        if (!(result instanceof Number number)
                || number.intValue() != 1) {

            throw new ApiException(

                    HttpStatus.CONFLICT,

                    "No fue posible eliminar la relación "
                            + "entre el perfil y la persona."

            );
        }
    }

    /*
    * CONVERTIR PERSONA A DTO
    */

    private PerfilPersonaDTO mapPersonaToDTO(

            PersonaGeneralEntity entity

    ) {

        PersonaNaturalGeneralEntity persona =
                entity.getPersonaNaturalGeneral();

        String nombreCompleto = "";

        if (persona != null) {

            nombreCompleto = Stream.of(

                    persona.getPrimerNombre(),

                    persona.getSegundoNombre(),

                    persona.getPrimerApellido(),

                    persona.getSegundoApellido()

            )

            .filter(Objects::nonNull)

            .map(String::trim)

            .filter(value -> !value.isEmpty())

            .collect(Collectors.joining(" "));
        }

        return new PerfilPersonaDTO(

                entity.getId(),

                entity.getDocumentoIdentidad(),

                nombreCompleto

        );
    }





}