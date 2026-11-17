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

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.PersonaAutorizaConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.PersonaProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ProyectosMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DiaSemanaOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FuncionProyectoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NivelProyectoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ProyectosEntity;
import co.edu.unipamplona.ciadti.rvd.model.enums.DiaSemanaEnum;
import co.edu.unipamplona.ciadti.rvd.model.enums.FuncionProyectoEnum;
import co.edu.unipamplona.ciadti.rvd.model.enums.NivelProyectoEnum;
import co.edu.unipamplona.ciadti.rvd.model.repository.CentroCostoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
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
    private final PersonaGeneralRepository personaGeneralRepository;
    private final ProyectosMapper proyectosMapper;
    private final PersonaProyectoMapper personaProyectoMapper;
    private final PersonaAutorizaConvocatoriaMapper personaAutorizaConvocatoriaMapper;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
                .map(row -> new TipoActividadProyectoDTO(
                        row.getId(),
                        row.getIdPadre(),
                        row.getNombre(),
                        row.getNombrePadre(),
                        row.getNombrePadre() != null && !row.getNombrePadre().isBlank()
                                ? row.getNombrePadre() + " - " + row.getNombre()
                                : row.getNombre(),
                        row.getDescripcion(),
                        row.getComponente(),
                        Integer.valueOf(1).equals(row.getEsSeleccionable())
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

        if (targetCoordinacionId != null) {
            return centroCostoRepository.findByCoordinacionId(targetCoordinacionId).stream()
                    .map(item -> new CentroCostoOpcionDTO(item.getId(), item.getCodigo(), item.getLabel()))
                    .toList();
        }

        return centroCostoRepository.findAdministrationOptions().stream()
                .map(item -> new CentroCostoOpcionDTO(item.getId(), item.getCodigo(), item.getLabel()))
                .toList();
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
        if (!StringUtils.hasText(normalized) || normalized.length() < 2) {
            return Collections.emptyList();
        }

        List<PersonaGeneralEntity> persons = personaGeneralRepository.searchGeneralPerson(normalized, normalized);
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

        String jsonData = buildJsonData(null, dto.data(), dto.funcion(), dto.nivel());
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

        validateParticipant(dto);
        entity.setIdProyecto(dto.idProyecto());
        entity.setIdPersonaGeneral(dto.idPersonaGeneral());
        entity.setHoras(StringUtils.hasText(dto.horas()) ? dto.horas().trim() : entity.getHoras());
        entity.setObservacion(dto.observacion() != null ? dto.observacion().trim() : null);
        entity.setIdCentroCosto(dto.idCentroCosto());
        entity.setIdPrograma(dto.idPrograma());

        String jsonDataUpdate = buildJsonData(entity.getData(), dto.data(), dto.funcion(), dto.nivel());
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
            if (proyecto.getIdCoordinacion() != null) {
                long count = centroCostoRepository.countByCoordinacionIdAndCentroCostoId(
                        proyecto.getIdCoordinacion(),
                        dto.idCentroCosto()
                );
                if (count == 0) {
                    throw new ApiException(
                            HttpStatus.BAD_REQUEST,
                            "El centro de costo seleccionado no pertenece a la coordinación del proyecto"
                    );
                }
            }
        }

        if (StringUtils.hasText(dto.horas())) {
            try {
                int horas = Integer.parseInt(dto.horas().trim());
                if (horas < 1 || horas > 40) {
                    throw new ApiException(
                            HttpStatus.BAD_REQUEST,
                            "Las horas de dedicación deben estar entre 1 y 40"
                    );
                }
            } catch (NumberFormatException e) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas deben ser un número válido");
            }
        }
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

    private String buildJsonData(String existingData, String incomingData, String funcion, String nivel) {
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

        if (StringUtils.hasText(incomingData)) {
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
        } else if (incomingData != null) {
            map.remove("horario");
        }

        if (StringUtils.hasText(funcion)) {
            map.put("funcion", funcion.trim());
        } else if (funcion != null) {
            map.remove("funcion");
        }

        if (StringUtils.hasText(nivel)) {
            map.put("nivel", nivel.trim());
        } else if (nivel != null) {
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
}
