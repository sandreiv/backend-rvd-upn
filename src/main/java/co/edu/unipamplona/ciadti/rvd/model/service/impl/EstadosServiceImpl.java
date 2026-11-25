package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.EntityManager;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoSiguienteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.EstadoSiguienteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.EstadosEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.EstadosRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.EstadoSiguienteRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.EstadosService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadosServiceImpl implements EstadosService {

    private final EstadosRepository estadosRepository;
    private final EstadoSiguienteRepository estadoSiguienteRepository;
    private final EntityManager entityManager;

    private static final String ORDEN_ESTADO_INICIAL = "1";

    @Override
    @Transactional(readOnly = true)
    public List<EstadoDTO> listStates() {

        return estadosRepository.findAll(
                Sort.by(Sort.Direction.ASC, "id")
        ).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoDTO getStateById(Long id) {
        return mapToDTO(findState(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoDTO> listNextStates(Long idEstadoOrigen) {

        findState(idEstadoOrigen);

        return estadoSiguienteRepository
                .findAllByIdEstadoOrigen(idEstadoOrigen)
                .stream()
                .map(EstadoSiguienteEntity::getIdEstadoDestino)
                .distinct()
                .map(this::findState)
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public void saveNextStates(
            Long idEstadoOrigen,
            EstadoSiguienteFormularioDTO dto
    ) {

        if (idEstadoOrigen == null || idEstadoOrigen <= 0) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El identificador del estado de origen no es válido."
            );
        }

        if (dto == null || dto.estadosDestinoIds() == null) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Debe proporcionar la lista de estados siguientes."
            );
        }

        estadosRepository.findByIdForUpdate(idEstadoOrigen)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "El estado de origen no existe."
                ));

        if (dto.estadosDestinoIds().stream()
                .anyMatch(id -> id == null || id <= 0)) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Existen identificadores de estados no válidos."
            );
        }

        Set<Long> destinosSeleccionados =
                new LinkedHashSet<>(dto.estadosDestinoIds());

        if (destinosSeleccionados.contains(idEstadoOrigen)) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Un estado no puede ser su propio estado siguiente."
            );
        }

        List<EstadosEntity> estadosDestino =
                estadosRepository.findAllById(destinosSeleccionados);

        if (estadosDestino.size() != destinosSeleccionados.size()) {

            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "Uno o más estados seleccionados no existen."
            );
        }

        List<EstadoSiguienteEntity> relacionesActuales =
                estadoSiguienteRepository
                        .findAllByIdEstadoOrigen(idEstadoOrigen);

        Set<Long> destinosConservados = new HashSet<>();

        List<EstadoSiguienteEntity> relacionesEliminar =
                new ArrayList<>();

        for (EstadoSiguienteEntity relacion : relacionesActuales) {

            Long idDestino = relacion.getIdEstadoDestino();

            if (!destinosSeleccionados.contains(idDestino)) {

                relacionesEliminar.add(relacion);

            } else if (!destinosConservados.add(idDestino)) {

                relacionesEliminar.add(relacion);
            }
        }

        List<EstadoSiguienteEntity> relacionesCrear =
                new ArrayList<>();

        for (Long idDestino : destinosSeleccionados) {

            if (destinosConservados.contains(idDestino)) {
                continue;
            }

            EstadoSiguienteEntity relacion =
                    new EstadoSiguienteEntity();

            relacion.setIdEstadoOrigen(idEstadoOrigen);
            relacion.setIdEstadoDestino(idDestino);

            relacion.setRegistradoPor(
                    RegistradoPorUtils.value(Accion.INSERT)
            );

            relacion.setFechaCambio(new Date());

            relacionesCrear.add(relacion);
        }

        try {

            if (!relacionesEliminar.isEmpty()) {

                estadoSiguienteRepository.deleteAll(
                        relacionesEliminar
                );

                estadoSiguienteRepository.flush();
            }

            if (!relacionesCrear.isEmpty()) {

                estadoSiguienteRepository.saveAll(
                        relacionesCrear
                );
            }

            estadoSiguienteRepository.flush();

        } catch (DataIntegrityViolationException ex) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No fue posible guardar los estados siguientes "
                    + "debido a una restricción de integridad."
            );
        }
    }

    @Override
    @Transactional
    public void saveState(EstadoFormularioDTO dto) {

        validateForm(dto);

        validateInitialOrder(dto.orden().trim(), null);

        EstadosEntity entity = new EstadosEntity();

        applyForm(entity, dto);

        entity.setRegistradoPor(
                RegistradoPorUtils.value(Accion.INSERT)
        );

        entity.setFechaCambio(new Date());

        estadosRepository.save(entity);
    }

    @Override
    @Transactional
    public void updateState(
            Long id,
            EstadoFormularioDTO dto
    ) {

        validateForm(dto);

        EstadosEntity entity = findState(id);

        String nuevoOrden = dto.orden().trim();

        if (ORDEN_ESTADO_INICIAL.equals(entity.getOrden())
                && !ORDEN_ESTADO_INICIAL.equals(nuevoOrden)) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No se puede modificar el orden del estado inicial."
            );
        }

        validateInitialOrder(nuevoOrden, id);

        applyForm(entity, dto);

        entity.setRegistradoPor(
                RegistradoPorUtils.value(Accion.UPDATE)
        );

        entity.setFechaCambio(new Date());

        estadosRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteState(Long id) {

        EstadosEntity entity = findState(id);

        validateDeletion(entity);

        try {

            estadosRepository.delete(entity);
            estadosRepository.flush();

        } catch (DataIntegrityViolationException ex) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el estado porque "
                    + "tiene registros asociados."
            );
        }
    }

    @Override
    @Transactional
    public void deleteStates(List<Long> ids) {

        if (ids == null
                || ids.isEmpty()
                || ids.stream().anyMatch(
                        id -> id == null || id <= 0
                )) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Debe seleccionar estados válidos."
            );
        }

        List<EstadosEntity> entities = ids.stream()
                .distinct()
                .map(this::findState)
                .toList();

        // Validar todos antes de eliminar.
        entities.forEach(this::validateDeletion);

        try {

            estadosRepository.deleteAll(entities);
            estadosRepository.flush();

        } catch (DataIntegrityViolationException ex) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No se pudieron eliminar los estados "
                    + "porque existen registros asociados."
            );
        }
    }

    private EstadosEntity findState(Long id) {

        if (id == null || id <= 0) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El identificador del estado es obligatorio."
            );
        }

        return estadosRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Estado no encontrado."
                ));
    }

    private void applyForm(
            EstadosEntity entity,
            EstadoFormularioDTO dto
    ) {

        entity.setNombre(dto.nombre().trim());
        entity.setDescripcion(dto.descripcion().trim());
        entity.setTipo(dto.tipo().trim());
        entity.setOrden(dto.orden().trim());
    }

    private void validateForm(EstadoFormularioDTO dto) {

        if (dto == null) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La información del estado es obligatoria."
            );
        }

        if (!StringUtils.hasText(dto.nombre())) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre del estado es obligatorio."
            );
        }

        if (!StringUtils.hasText(dto.descripcion())) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La descripción del estado es obligatoria."
            );
        }

        if (!StringUtils.hasText(dto.tipo())) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo del estado es obligatorio."
            );
        }

        if (!StringUtils.hasText(dto.orden())) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El orden del estado es obligatorio."
            );
        }

        if (dto.tipo().trim().length() > 30) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo no puede superar los 30 caracteres."
            );
        }

        if (dto.orden().trim().length() > 30) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El orden no puede superar los 30 caracteres."
            );
        }
    }

    private void validateInitialOrder(
            String orden,
            Long id
    ) {

        if (!ORDEN_ESTADO_INICIAL.equals(orden)) {
            return;
        }

        estadosRepository.findFirstByOrden(orden)
                .ifPresent(existing -> {

                    if (!Objects.equals(existing.getId(), id)) {

                        throw new ApiException(
                                HttpStatus.CONFLICT,
                                "Ya existe un estado con el orden inicial."
                        );
                    }
                });
    }

    private void validateDeletion(EstadosEntity entity) {

        if (ORDEN_ESTADO_INICIAL.equals(entity.getOrden())) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el estado inicial."
            );
        }

        Long id = entity.getId();

        boolean hasReferences =
                hasReferences(
                        """
                        SELECT COUNT(c)
                        FROM CargaDocenteEntity c
                        WHERE c.idEstado = :id
                        """,
                        id
                )
                || hasReferences(
                        """
                        SELECT COUNT(e)
                        FROM EstadoCargaEntity e
                        WHERE e.idEstadoAnterior = :id
                           OR e.idEstadoNuevo = :id
                        """,
                        id
                )
                || hasReferences(
                        """
                        SELECT COUNT(e)
                        FROM EstadoSiguienteEntity e
                        WHERE e.idEstadoOrigen = :id
                           OR e.idEstadoDestino = :id
                        """,
                        id
                )
                || hasReferences(
                        """
                        SELECT COUNT(p)
                        FROM PerfilEstadoEntity p
                        WHERE p.idEstado = :id
                        """,
                        id
                );

        if (hasReferences) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el estado porque "
                    + "tiene registros asociados."
            );
        }
    }

    private boolean hasReferences(String query, Long id) {

        return entityManager
                .createQuery(query, Long.class)
                .setParameter("id", id)
                .getSingleResult() > 0;
    }

    private EstadoDTO mapToDTO(EstadosEntity entity) {

        return new EstadoDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getTipo(),
                entity.getOrden()
        );
    }
}