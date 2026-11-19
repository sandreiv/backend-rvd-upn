/**
 * Aplicación: rvd
 * Archivo: ProyectosServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Leonel Antonio Perez Rios - Creación inicial
 * 10/11/2026 - Leonel Antonio Perez Rios - Búsqueda de personas para proyectos
 * 10/11/2026 - Leonel Antonio Perez Rios - Soporte para listado completo jerárquico de proyectos y productos
 * 10/11/2026 - Leonel Antonio Perez Rios - Refactorización de código limpio, mensajes de error y eliminación de constantes innecesarias
 * 13/11/2026 - Remoción de logs (se gestionan por aspecto) y soporte de centro de costo y funcion en data
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.PersonaAutorizaConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.PersonaProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ProyectosMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DiaSemanaOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstudianteBusquedaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstudianteProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstudianteProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FuncionProyectoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NivelProyectoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadComponenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.EstudiantesEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ProyectosEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoActividadesEntity;
import co.edu.unipamplona.ciadti.rvd.model.enums.DiaSemanaEnum;
import co.edu.unipamplona.ciadti.rvd.model.enums.FuncionProyectoEnum;
import co.edu.unipamplona.ciadti.rvd.model.enums.NivelProyectoEnum;
import co.edu.unipamplona.ciadti.rvd.model.enums.TipoActividadComponenteEnum;
import co.edu.unipamplona.ciadti.rvd.model.repository.CentroCostoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.EstudiantesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProyectoGrupoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProyectosRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaProyectoListaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProyectosListaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadProyectoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.ProyectosService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProyectosServiceImpl implements ProyectosService {

    private final ProyectosRepository proyectosRepository;
    private final TipoActividadesRepository tipoActividadesRepository;
    private final CoordinacionRepository coordinacionRepository;
    private final CentroCostoRepository centroCostoRepository;
    private final ProyectoGrupoRepository proyectoGrupoRepository;
    private final PersonaProyectoRepository personaProyectoRepository;
    private final EstudiantesRepository estudiantesRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final ProyectosMapper proyectosMapper;
    private final PersonaProyectoMapper personaProyectoMapper;
    private final PersonaAutorizaConvocatoriaMapper personaAutorizaConvocatoriaMapper;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int MIN_SEARCH_TERM_LENGTH = 2;
    private static final int MIN_HORAS = 1;
    private static final int MAX_HORAS = 40;

    @Override
    @Transactional(readOnly = true)
    public List<ProyectosListaDTO> listProjects() {
        List<ProyectosListaProjection> projects = proyectosRepository.findParentProjectsList();
        return proyectosMapper.toProyectosListaDTOList(projects);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectosListaDTO> listAllProjects() {
        List<ProyectosListaProjection> rows = proyectosRepository.findAllProjectsList();
        return proyectosMapper.toProyectosListaDTOList(rows);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectosListaDTO> listProducts(Long idProyecto) {
        if (idProyecto == null || !proyectosRepository.existsById(idProyecto)) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe el proyecto con id " + idProyecto
            );
        }
        List<ProyectosListaProjection> products = proyectosRepository.findProductsByParentProjectId(idProyecto);
        return proyectosMapper.toProyectosListaDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadProyectoDTO> listProjectActivityTypes() {
        List<TipoActividadProyectoProjection> rows = tipoActividadesRepository.findProjectActivityOptions();
        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }

        return rows.stream()
                .map(row -> {
                    TipoActividadComponenteEnum compEnum = TipoActividadComponenteEnum.fromComponente(row.getComponente());
                    boolean reqFuncion = compEnum != null && compEnum.isRequiereFuncion();
                    boolean reqNivel = compEnum != null && compEnum.isRequiereNivel();
                    boolean reqHorario = compEnum != null && compEnum.isRequiereHorario();

                    Integer minHoras = parseInteger(row.getMinimoHoras());
                    Integer maxHoras = parseInteger(row.getMaximoHoras());

                    return new TipoActividadProyectoDTO(
                            row.getId(),
                            row.getIdPadre(),
                            row.getNombre(),
                            row.getNombrePadre(),
                            row.getNombrePadre() != null && !row.getNombrePadre().isBlank()
                                    ? row.getNombrePadre() + " - " + row.getNombre()
                                    : row.getNombre(),
                            row.getDescripcion(),
                            row.getComponente(),
                            Integer.valueOf(1).equals(row.getEsSeleccionable()),
                            reqFuncion,
                            reqNivel,
                            reqHorario,
                            minHoras,
                            maxHoras
                    );
                })
                .toList();
    }

    @Override
    public List<TipoActividadComponenteDTO> listActivityComponents() {
        return Arrays.stream(TipoActividadComponenteEnum.values())
                .map(c -> new TipoActividadComponenteDTO(
                        c.name(),
                        c.getDescripcion(),
                        c.isRequiereFuncion(),
                        c.isRequiereNivel(),
                        c.isRequiereHorario()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionOpcionDTO> searchCoordinations(String term) {
        String normalized = StringUtils.hasText(term) ? term.trim() : null;
        return proyectosRepository.searchCoordinations(normalized).stream()
                .map(p -> new CoordinacionOpcionDTO(p.getId(), p.getNombre(), p.getDescripcion()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CentroCostoOpcionDTO> listCostCenters(Long idProyecto, Long idCoordinacion) {
        Long targetCoordinacionId = idCoordinacion;
        if (targetCoordinacionId == null && idProyecto != null) {
            targetCoordinacionId = proyectosRepository.findById(idProyecto)
                    .map(ProyectosEntity::getIdCoordinacion)
                    .orElse(null);
        }

        Map<Long, CentroCostoOpcionDTO> optionsMap = new LinkedHashMap<>();

        if (targetCoordinacionId != null) {
            centroCostoRepository.findByCoordinacionId(targetCoordinacionId).forEach(item ->
                    optionsMap.put(item.getId(), new CentroCostoOpcionDTO(item.getId(), item.getCodigo(), item.getLabel()))
            );
        } else {
            centroCostoRepository.findAdministrationOptions().forEach(item ->
                    optionsMap.put(item.getId(), new CentroCostoOpcionDTO(item.getId(), item.getCodigo(), item.getLabel()))
            );
        }

        if (idProyecto != null) {
            centroCostoRepository.findCostCentersByProjectStudents(idProyecto).forEach(item -> {
                if (!optionsMap.containsKey(item.getId())) {
                    optionsMap.put(item.getId(), new CentroCostoOpcionDTO(item.getId(), item.getCodigo(), item.getLabel()));
                }
            });
        }

        return new ArrayList<>(optionsMap.values());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoGrupoOpcionDTO> listProjectGroupOptions() {
        return proyectoGrupoRepository.findAllActiveOrderByNombre().stream()
                .map(pg -> new ProyectoGrupoOpcionDTO(pg.getId(), pg.getNombre(), pg.getDescripcion()))
                .toList();
    }

    @Override
    public List<FuncionProyectoOpcionDTO> listParticipantFunctions() {
        return Arrays.stream(FuncionProyectoEnum.values())
                .map(f -> new FuncionProyectoOpcionDTO(f.name(), f.getDescripcion()))
                .toList();
    }

    @Override
    public List<NivelProyectoOpcionDTO> listParticipantLevels() {
        return Arrays.stream(NivelProyectoEnum.values())
                .map(n -> new NivelProyectoOpcionDTO(n.name(), n.getDescripcion()))
                .toList();
    }

    @Override
    public List<DiaSemanaOpcionDTO> listDaysOfWeek() {
        return Arrays.stream(DiaSemanaEnum.values())
                .map(d -> new DiaSemanaOpcionDTO(d.name(), d.getNombre(), d.getAbreviacion()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaAutorizaConvocatoriaDTO> searchPersons(String term) {
        String normalized = StringUtils.hasText(term) ? term.trim() : null;
        if (!StringUtils.hasText(normalized) || normalized.length() < MIN_SEARCH_TERM_LENGTH) {
            return Collections.emptyList();
        }

        List<PersonaGeneralEntity> persons = personaGeneralRepository.searchByTerm(normalized);
        return personaAutorizaConvocatoriaMapper.toDtoList(persons);
    }

    @Override
    @Transactional
    public void saveProject(ProyectosFormularioDTO dto) {
        validateProject(dto);

        ProyectosEntity entity = new ProyectosEntity();
        mapDtoToEntity(dto, entity);
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        entity.setFechaCambio(new Date());

        proyectosRepository.save(entity);
    }

    @Override
    @Transactional
    public void updateProject(Long id, ProyectosFormularioDTO dto) {
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador del proyecto es obligatorio");
        }

        ProyectosEntity entity = proyectosRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el proyecto con id " + id
                ));

        validateProject(dto);
        mapDtoToEntity(dto, entity);
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        entity.setFechaCambio(new Date());

        proyectosRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador del proyecto es obligatorio");
        }

        ProyectosEntity entity = proyectosRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el proyecto con id " + id
                ));

        if (proyectosRepository.existsByIdProyectoPadre(id)) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el proyecto porque tiene subproyectos asociados"
            );
        }

        if (personaProyectoRepository.existsByIdProyecto(id)) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el proyecto porque tiene participantes vinculados"
            );
        }

        proyectosRepository.delete(entity);
    }

    @Override
    @Transactional
    public void deleteBulkProjects(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        for (Long id : ids) {
            if (id != null && proyectosRepository.existsById(id)) {
                deleteProject(id);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaProyectoListaDTO> listProjectParticipants(Long idProyecto) {
        if (idProyecto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador del proyecto es obligatorio");
        }

        List<PersonaProyectoListaProjection> projections = personaProyectoRepository.findByIdProyecto(idProyecto);
        return personaProyectoMapper.toPersonaProyectoListaDTOList(projections);
    }

    @Override
    @Transactional
    public void saveProjectParticipant(PersonaProyectoFormularioDTO dto) {
        validateParticipant(dto);

        PersonaProyectoEntity entity = new PersonaProyectoEntity();
        entity.setIdProyecto(dto.idProyecto());
        entity.setIdPersonaGeneral(dto.idPersonaGeneral());
        entity.setHoras(StringUtils.hasText(dto.horas()) ? dto.horas().trim() : "0");
        entity.setObservacion(dto.observacion() != null ? dto.observacion().trim() : null);
        entity.setIdCentroCosto(dto.idCentroCosto());
        entity.setIdPrograma(dto.idPrograma());

        TipoActividadComponenteEnum compEnum = resolveComponentEnum(dto.idProyecto());
        String jsonData = buildJsonData(null, dto.data(), dto.funcion(), dto.nivel(), compEnum);
        entity.setData(jsonData);

        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        entity.setFechaCambio(new Date());

        personaProyectoRepository.save(entity);
    }

    @Override
    @Transactional
    public void updateProjectParticipant(Long id, PersonaProyectoFormularioDTO dto) {
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador de asignación es obligatorio");
        }

        PersonaProyectoEntity entity = personaProyectoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Asignación no encontrada"));

        validateParticipant(dto, entity);
        entity.setIdProyecto(dto.idProyecto());
        entity.setIdPersonaGeneral(dto.idPersonaGeneral());
        entity.setHoras(StringUtils.hasText(dto.horas()) ? dto.horas().trim() : entity.getHoras());
        entity.setObservacion(dto.observacion() != null ? dto.observacion().trim() : null);
        entity.setIdCentroCosto(dto.idCentroCosto());
        entity.setIdPrograma(dto.idPrograma());

        TipoActividadComponenteEnum compEnum = resolveComponentEnum(dto.idProyecto());
        String jsonDataUpdate = buildJsonData(entity.getData(), dto.data(), dto.funcion(), dto.nivel(), compEnum);
        entity.setData(jsonDataUpdate);

        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        entity.setFechaCambio(new Date());

        personaProyectoRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteProjectParticipant(Long id) {
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador de asignación es obligatorio");
        }

        PersonaProyectoEntity entity = personaProyectoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Asignación no encontrada"));

        personaProyectoRepository.delete(entity);
    }

    @Override
    @Transactional
    public void deleteBulkProjectParticipants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        for (Long id : ids) {
            if (id != null) {
                deleteProjectParticipant(id);
            }
        }
    }

    private void validateProject(ProyectosFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información del proyecto es requerida");
        }

        if (!StringUtils.hasText(dto.nombre())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El nombre del proyecto es obligatorio");
        }

        if (dto.idTipoActividad() == null || !tipoActividadesRepository.existsById(dto.idTipoActividad())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de actividad es obligatorio o no existe");
        }

        if (dto.idCoordinacion() == null || !coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación es obligatoria o no existe");
        }

        if (dto.idProyectoPadre() != null && !proyectosRepository.existsById(dto.idProyectoPadre())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El proyecto padre no existe");
        }
    }

    private void validateParticipant(PersonaProyectoFormularioDTO dto) {
        validateParticipant(dto, null);
    }

    private void validateParticipant(PersonaProyectoFormularioDTO dto, PersonaProyectoEntity existingEntity) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información del participante es requerida");
        }

        if (dto.idProyecto() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El proyecto asociado es requerido");
        }

        ProyectosEntity proyecto = proyectosRepository.findById(dto.idProyecto())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "El proyecto asociado no existe"));

        if (dto.idPersonaGeneral() == null || !personaGeneralRepository.existsById(dto.idPersonaGeneral())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La persona no existe en el sistema");
        }

        if (dto.idCentroCosto() != null) {
            if (!centroCostoRepository.existsById(dto.idCentroCosto())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El centro de costo seleccionado no existe");
            }
            boolean valid = false;
            if (proyecto.getIdCoordinacion() != null) {
                long count = centroCostoRepository.countByCoordinacionIdAndCentroCostoId(
                        proyecto.getIdCoordinacion(),
                        dto.idCentroCosto()
                );
                if (count > 0) {
                    valid = true;
                }
            }
            if (!valid && proyecto.getId() != null) {
                long countEstudiantes = centroCostoRepository.countByProjectStudentsAndCentroCostoId(
                        proyecto.getId(),
                        dto.idCentroCosto()
                );
                if (countEstudiantes > 0) {
                    valid = true;
                }
            }
            if (!valid) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "El centro de costo seleccionado no pertenece a la coordinación del proyecto ni a los programas de los estudiantes asociados"
                );
            }
        }

        TipoActividadesEntity tipoActividad = null;
        if (proyecto.getIdTipoActividad() != null) {
            tipoActividad = tipoActividadesRepository.findById(proyecto.getIdTipoActividad()).orElse(null);
        }

        Integer minHoras = null;
        Integer maxHoras = null;
        if (tipoActividad != null) {
            minHoras = parseInteger(tipoActividad.getMinimoHoras());
            maxHoras = parseInteger(tipoActividad.getMaximoHoras());
            if ((minHoras == null || maxHoras == null) && tipoActividad.getIdPadre() != null) {
                TipoActividadesEntity padre = tipoActividadesRepository.findById(tipoActividad.getIdPadre()).orElse(null);
                if (padre != null) {
                    if (minHoras == null) {
                        minHoras = parseInteger(padre.getMinimoHoras());
                    }
                    if (maxHoras == null) {
                        maxHoras = parseInteger(padre.getMaximoHoras());
                    }
                }
            }
        }
        int effectiveMin = minHoras != null && minHoras > 0 ? minHoras : MIN_HORAS;
        int effectiveMax = maxHoras != null && maxHoras > 0 ? maxHoras : MAX_HORAS;

        if (StringUtils.hasText(dto.horas())) {
            try {
                int horas = Integer.parseInt(dto.horas().trim());
                if (horas < effectiveMin || horas > effectiveMax) {
                    String scope = (tipoActividad != null && StringUtils.hasText(tipoActividad.getNombre()))
                            ? " para " + tipoActividad.getNombre()
                            : "";
                    throw new ApiException(
                            HttpStatus.BAD_REQUEST,
                            "Las horas de dedicación deben estar entre " + effectiveMin + " y " + effectiveMax + scope
                    );
                }
            } catch (NumberFormatException e) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas deben ser un número válido");
            }
        }

        TipoActividadComponenteEnum compEnum = null;
        if (tipoActividad != null) {
            String comp = tipoActividad.getComponente();
            if (!StringUtils.hasText(comp) && tipoActividad.getIdPadre() != null) {
                comp = tipoActividadesRepository.findById(tipoActividad.getIdPadre())
                        .map(TipoActividadesEntity::getComponente)
                        .orElse(null);
            }
            compEnum = TipoActividadComponenteEnum.fromComponente(comp);
        }

        if (compEnum != null) {
            if (compEnum.isRequiereFuncion() && !StringUtils.hasText(dto.funcion())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "La función en el proyecto es obligatoria para este tipo de actividad");
            }
            if (compEnum.isRequiereNivel() && !StringUtils.hasText(dto.nivel())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El nivel académico es obligatorio para este tipo de actividad");
            }
            if (compEnum.isRequiereHorario()) {
                validateScheduleHours(dto, existingEntity);
            }
        }
    }

    private void validateScheduleHours(PersonaProyectoFormularioDTO dto, PersonaProyectoEntity existingEntity) {
        if (!StringUtils.hasText(dto.horas())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas de dedicación son obligatorias");
        }

        double assignedHours;
        try {
            assignedHours = Double.parseDouble(dto.horas().trim());
        } catch (NumberFormatException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas deben ser un número válido");
        }

        String dataJson = StringUtils.hasText(dto.data())
                ? dto.data().trim()
                : (existingEntity != null ? existingEntity.getData() : null);

        if (!StringUtils.hasText(dataJson)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Debe configurar el horario semanal para este tipo de actividad y debe sumar las " + formatHours(assignedHours) + " horas asignadas"
            );
        }

        try {
            JsonNode root = OBJECT_MAPPER.readTree(dataJson);
            JsonNode horarioNode = root.get("horario");
            if (horarioNode == null || !horarioNode.isArray() || horarioNode.isEmpty()) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "Debe configurar el horario semanal para este tipo de actividad y debe sumar las " + formatHours(assignedHours) + " horas asignadas"
                );
            }

            int totalMinutes = 0;
            for (JsonNode slot : horarioNode) {
                String inicio = slot.hasNonNull("horaInicio") ? slot.get("horaInicio").asText().trim() : null;
                String fin = slot.hasNonNull("horaFin") ? slot.get("horaFin").asText().trim() : null;
                if (!StringUtils.hasText(inicio) || !StringUtils.hasText(fin)) {
                    continue;
                }
                int diff = calculateSlotMinutes(inicio, fin);
                if (diff > 0) {
                    totalMinutes += diff;
                }
            }

            double totalHours = totalMinutes / 60.0;
            if (Math.abs(totalHours - assignedHours) >= 0.01) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "Las horas del horario semanal (" + formatHours(totalHours) + " hrs) deben ser iguales a las horas asignadas (" + formatHours(assignedHours) + " hrs)"
                );
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Formato de horario semanal inválido");
        }
    }

    private int calculateSlotMinutes(String inicio, String fin) {
        try {
            String[] parts1 = inicio.split(":");
            String[] parts2 = fin.split(":");
            int h1 = Integer.parseInt(parts1[0].trim());
            int m1 = parts1.length > 1 ? Integer.parseInt(parts1[1].trim()) : 0;
            int h2 = Integer.parseInt(parts2[0].trim());
            int m2 = parts2.length > 1 ? Integer.parseInt(parts2[1].trim()) : 0;
            int diff = (h2 * 60 + m2) - (h1 * 60 + m1);
            return Math.max(diff, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    private String formatHours(double hours) {
        if (Math.abs(hours - Math.round(hours)) < 0.001) {
            return String.valueOf((long) Math.round(hours));
        }
        return String.format(java.util.Locale.US, "%.1f", hours);
    }

    private TipoActividadComponenteEnum resolveComponentEnum(Long idProyecto) {
        if (idProyecto == null) {
            return null;
        }
        return proyectosRepository.findById(idProyecto)
                .map(ProyectosEntity::getIdTipoActividad)
                .flatMap(tipoActividadesRepository::findById)
                .map(ta -> {
                    String comp = ta.getComponente();
                    if (!StringUtils.hasText(comp) && ta.getIdPadre() != null) {
                        comp = tipoActividadesRepository.findById(ta.getIdPadre())
                                .map(TipoActividadesEntity::getComponente)
                                .orElse(null);
                    }
                    return TipoActividadComponenteEnum.fromComponente(comp);
                })
                .orElse(null);
    }

    private void mapDtoToEntity(ProyectosFormularioDTO dto, ProyectosEntity entity) {
        entity.setCodigo(StringUtils.hasText(dto.codigo()) ? dto.codigo().trim() : null);
        entity.setNombre(dto.nombre().trim());
        entity.setDescripcion(dto.descripcion() != null ? dto.descripcion().trim() : null);
        entity.setNumeroActa(StringUtils.hasText(dto.numeroActa()) ? dto.numeroActa().trim() : null);
        entity.setFechaActa(dto.fechaActa());
        entity.setActaConsejo(dto.actaConsejo() != null ? dto.actaConsejo().trim() : dto.numeroActa());
        entity.setActivo(StringUtils.hasText(dto.activo()) ? dto.activo().trim() : "1");
        entity.setIdTipoActividad(dto.idTipoActividad());
        entity.setIdCoordinacion(dto.idCoordinacion());
        entity.setIdProyectoPadre(dto.idProyectoPadre());
        entity.setIdMateriaParticular(dto.idMateriaParticular());
        entity.setIdProyectoGrupo(dto.idProyectoGrupo());
    }

    private String buildJsonData(
            String existingData,
            String incomingData,
            String funcion,
            String nivel,
            TipoActividadComponenteEnum compEnum
    ) {
        Map<String, Object> map = new LinkedHashMap<>();

        if (StringUtils.hasText(existingData)) {
            try {
                Map<String, Object> existingMap = OBJECT_MAPPER.readValue(existingData.trim(), new TypeReference<Map<String, Object>>() {});
                if (existingMap != null) {
                    map.putAll(existingMap);
                }
            } catch (Exception ignored) {
            }
        }

        boolean allowHorario = compEnum == null || compEnum.isRequiereHorario();
        boolean allowFuncion = compEnum == null || compEnum.isRequiereFuncion();
        boolean allowNivel = compEnum == null || compEnum.isRequiereNivel();

        if (allowHorario && StringUtils.hasText(incomingData)) {
            try {
                Map<String, Object> incomingMap = OBJECT_MAPPER.readValue(incomingData.trim(), new TypeReference<Map<String, Object>>() {});
                if (incomingMap != null) {
                    if (!incomingMap.containsKey("horario")) {
                        map.remove("horario");
                    }
                    map.putAll(incomingMap);
                }
            } catch (Exception ignored) {
            }
        } else {
            map.remove("horario");
        }

        if (allowFuncion && StringUtils.hasText(funcion)) {
            map.put("funcion", funcion.trim());
        } else {
            map.remove("funcion");
        }

        if (allowNivel && StringUtils.hasText(nivel)) {
            map.put("nivel", nivel.trim());
        } else {
            map.remove("nivel");
        }

        if (map.isEmpty()) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteProyectoListaDTO> listProjectStudents(Long idProyecto) {
        if (idProyecto == null) {
            return Collections.emptyList();
        }
        return estudiantesRepository.findStudentsByProjectId(idProyecto).stream()
                .map(p -> new EstudianteProyectoListaDTO(
                        p.getId(),
                        p.getIdProyecto(),
                        p.getIdPersonaGeneral(),
                        p.getIdEstudiantePensum(),
                        p.getDocumento(),
                        p.getNombreCompleto(),
                        p.getIdPrograma(),
                        p.getNombrePrograma(),
                        p.getIdCentroCosto(),
                        p.getNombreCentroCosto()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteBusquedaDTO> searchStudents(String term) {
        String normalized = StringUtils.hasText(term) ? term.trim() : null;
        if (!StringUtils.hasText(normalized) || normalized.length() < MIN_SEARCH_TERM_LENGTH) {
            return Collections.emptyList();
        }
        return estudiantesRepository.searchStudents(normalized).stream()
                .map(p -> new EstudianteBusquedaDTO(
                        p.getIdPersonaGeneral(),
                        p.getDocumento(),
                        p.getNombreCompleto(),
                        p.getIdEstudiantePensum(),
                        p.getIdPrograma(),
                        p.getNombrePrograma()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void saveProjectStudent(EstudianteProyectoFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información del estudiante es requerida");
        }
        if (dto.idProyecto() == null || !proyectosRepository.existsById(dto.idProyecto())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El proyecto asociado no existe");
        }
        if (dto.idPersonaGeneral() == null || !personaGeneralRepository.existsById(dto.idPersonaGeneral())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La persona no existe en el sistema");
        }

        long count = estudiantesRepository.countByProyectoIdAndPersonaGeneralId(dto.idProyecto(), dto.idPersonaGeneral());
        if (count > 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El estudiante ya se encuentra asociado a este proyecto");
        }

        EstudiantesEntity entity = new EstudiantesEntity();
        entity.setIdProyecto(dto.idProyecto());
        entity.setIdPersonaGeneral(dto.idPersonaGeneral());
        entity.setIdEstudiantePensum(dto.idEstudiantePensum());

        estudiantesRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteProjectStudent(Long id) {
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador del registro es obligatorio");
        }
        if (!estudiantesRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el estudiante asociado con id " + id);
        }
        estudiantesRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBulkProjectStudents(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        estudiantesRepository.deleteAllById(ids);
    }

    private Integer parseInteger(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
