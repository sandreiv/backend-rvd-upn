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
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.PersonaAutorizaConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.PersonaProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ProyectosMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ProyectosEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProyectosRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaProyectoListaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProyectosListaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadProyectoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.ProyectosService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProyectosServiceImpl implements ProyectosService {

    private static final String ESTADO_ACTIVO = "1";

    private final ProyectosRepository proyectosRepository;
    private final TipoActividadesRepository tipoActividadesRepository;
    private final CoordinacionRepository coordinacionRepository;
    private final PersonaProyectoRepository personaProyectoRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final ProyectosMapper proyectosMapper;
    private final PersonaProyectoMapper personaProyectoMapper;
    private final PersonaAutorizaConvocatoriaMapper personaAutorizaConvocatoriaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProyectosListaDTO> listProjects() {
        log.debug("listProjects ===> Listando proyectos padre");
        List<ProyectosListaProjection> projects = proyectosRepository.findParentProjectsList();
        List<ProyectosListaDTO> result = proyectosMapper.toProyectosListaDTOList(projects);
        log.info("listProjects ===> Proyectos padre listados. total={}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectosListaDTO> listAllProjects() {
        log.debug("listAllProjects ===> Listando todos los proyectos y productos");
        List<ProyectosListaProjection> rows = proyectosRepository.findAllProjectsList();
        List<ProyectosListaDTO> result = proyectosMapper.toProyectosListaDTOList(rows);
        log.info("listAllProjects ===> Proyectos y productos listados. total={}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectosListaDTO> listProducts(Long idProyecto) {
        log.debug("listProducts ===> Listando productos. idProyecto={}", idProyecto);
        if (idProyecto == null || !proyectosRepository.existsById(idProyecto)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El proyecto padre especificado no existe");
        }
        List<ProyectosListaProjection> products = proyectosRepository.findProductsByParentProjectId(idProyecto);
        List<ProyectosListaDTO> result = proyectosMapper.toProyectosListaDTOList(products);
        log.info("listProducts ===> Productos listados. total={}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadProyectoDTO> listProjectActivityTypes() {
        log.debug("listProjectActivityTypes ===> Listando actividades para proyectos");
        List<TipoActividadProyectoProjection> rows = tipoActividadesRepository.findProjectActivityOptions();
        List<TipoActividadProyectoDTO> result = rows.stream().map(row -> {
            String nombrePadre = row.getNombrePadre();
            String nombre = row.getNombre();
            String nombreCompleto = StringUtils.hasText(nombrePadre) ? nombrePadre + " > " + nombre : nombre;
            boolean seleccionable = row.getEsSeleccionable() != null && row.getEsSeleccionable() == 1;
            return new TipoActividadProyectoDTO(
                    row.getId(),
                    row.getIdPadre(),
                    nombre,
                    nombrePadre,
                    nombreCompleto,
                    row.getDescripcion(),
                    row.getComponente(),
                    seleccionable
            );
        }).toList();
        log.info("listProjectActivityTypes ===> Actividades listadas para proyectos. total={}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionOpcionDTO> searchCoordinations(String term) {
        log.debug("searchCoordinations ===> Buscando coordinaciones para proyectos. term={}", term);
        String normalized = StringUtils.hasText(term) ? term.trim() : null;
        List<co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionOpcionProjection> rows = proyectosRepository.searchCoordinations(normalized);
        List<CoordinacionOpcionDTO> result = rows.stream().map(row -> new CoordinacionOpcionDTO(
                row.getId(),
                row.getNombre(),
                row.getDescripcion()
        )).toList();
        log.info("searchCoordinations ===> Coordinaciones encontradas. total={}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaAutorizaConvocatoriaDTO> searchPersons(String term) {
        log.debug("searchPersons ===> Buscando personas para proyectos. term={}", term);
        if (term == null || term.trim().length() < 2) {
            return Collections.emptyList();
        }
        String cleanTerm = term.trim();
        String documento = cleanTerm.matches("\\d+") ? cleanTerm : null;
        String nombre = documento == null ? cleanTerm : null;
        List<PersonaGeneralEntity> list = personaGeneralRepository.searchGeneralPerson(nombre, documento);
        List<PersonaAutorizaConvocatoriaDTO> result = list.stream()
                .limit(30)
                .map(personaAutorizaConvocatoriaMapper::toDto)
                .toList();
        log.info("searchPersons ===> Personas encontradas. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveProject(ProyectosFormularioDTO dto) {
        log.debug("saveProject ===> Guardando proyecto: {}", dto.nombre());
        validateProject(dto, null);

        ProyectosEntity entity = new ProyectosEntity();
        mapDtoToEntity(dto, entity);
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        entity.setFechaCambio(new Date());

        proyectosRepository.save(entity);
        log.info("saveProject ===> Proyecto guardado con éxito. id={}", entity.getId());
    }

    @Override
    @Transactional
    public void updateProject(Long id, ProyectosFormularioDTO dto) {
        log.debug("updateProject ===> Actualizando proyecto id={}", id);
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador del proyecto es requerido");
        }

        ProyectosEntity entity = proyectosRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Proyecto no encontrado"));

        validateProject(dto, id);
        mapDtoToEntity(dto, entity);
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        entity.setFechaCambio(new Date());

        proyectosRepository.save(entity);
        log.info("updateProject ===> Proyecto actualizado con éxito. id={}", id);
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        log.debug("deleteProject ===> Eliminando proyecto id={}", id);
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador del proyecto es requerido");
        }

        ProyectosEntity entity = proyectosRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Proyecto no encontrado"));

        if (proyectosRepository.existsByIdProyectoPadre(id)) {
            throw new ApiException(HttpStatus.CONFLICT, "No se puede eliminar el proyecto porque tiene productos asociados");
        }

        if (personaProyectoRepository.existsByIdProyecto(id)) {
            throw new ApiException(HttpStatus.CONFLICT, "No se puede eliminar el proyecto porque tiene docentes asignados");
        }

        proyectosRepository.delete(entity);
        log.info("deleteProject ===> Proyecto eliminado con éxito. id={}", id);
    }

    @Override
    @Transactional
    public void deleteBulkProjects(List<Long> ids) {
        log.debug("deleteBulkProjects ===> Eliminación masiva. ids={}", ids);
        if (ids == null || ids.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Debe proporcionar al menos un ID para eliminar");
        }
        for (Long id : ids) {
            deleteProject(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaProyectoListaDTO> listProjectParticipants(Long idProyecto) {
        log.debug("listProjectParticipants ===> Listando participantes. idProyecto={}", idProyecto);
        if (idProyecto == null || !proyectosRepository.existsById(idProyecto)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El proyecto no existe");
        }
        List<PersonaProyectoListaProjection> participants = personaProyectoRepository.findByIdProyecto(idProyecto);
        return personaProyectoMapper.toPersonaProyectoListaDTOList(participants);
    }

    @Override
    @Transactional
    public void saveProjectParticipant(PersonaProyectoFormularioDTO dto) {
        log.debug("saveProjectParticipant ===> Asignando docente idPersonaGeneral={} a proyecto idProyecto={}",
                dto.idPersonaGeneral(), dto.idProyecto());
        validateParticipant(dto);

        PersonaProyectoEntity entity = new PersonaProyectoEntity();
        entity.setIdProyecto(dto.idProyecto());
        entity.setIdPersonaGeneral(dto.idPersonaGeneral());
        entity.setHoras(StringUtils.hasText(dto.horas()) ? dto.horas().trim() : "0");
        entity.setObservacion(dto.observacion() != null ? dto.observacion().trim() : null);
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        entity.setFechaCambio(new Date());

        personaProyectoRepository.save(entity);
        log.info("saveProjectParticipant ===> Docente asignado con éxito. id={}", entity.getId());
    }

    @Override
    @Transactional
    public void updateProjectParticipant(Long id, PersonaProyectoFormularioDTO dto) {
        log.debug("updateProjectParticipant ===> Actualizando participante id={}", id);
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador de asignación es requerido");
        }

        PersonaProyectoEntity entity = personaProyectoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Asignación no encontrada"));

        validateParticipant(dto);
        entity.setIdProyecto(dto.idProyecto());
        entity.setIdPersonaGeneral(dto.idPersonaGeneral());
        entity.setHoras(StringUtils.hasText(dto.horas()) ? dto.horas().trim() : entity.getHoras());
        entity.setObservacion(dto.observacion() != null ? dto.observacion().trim() : null);
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        entity.setFechaCambio(new Date());

        personaProyectoRepository.save(entity);
        log.info("updateProjectParticipant ===> Asignación actualizada con éxito. id={}", id);
    }

    @Override
    @Transactional
    public void deleteProjectParticipant(Long id) {
        log.debug("deleteProjectParticipant ===> Eliminando participante id={}", id);
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador de asignación es requerido");
        }
        PersonaProyectoEntity entity = personaProyectoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Asignación no encontrada"));
        personaProyectoRepository.delete(entity);
    }

    @Override
    @Transactional
    public void deleteBulkProjectParticipants(List<Long> ids) {
        log.debug("deleteBulkProjectParticipants ===> Eliminación masiva participantes. ids={}", ids);
        if (ids == null || ids.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Debe proporcionar al menos un ID para eliminar");
        }
        for (Long id : ids) {
            deleteProjectParticipant(id);
        }
    }

    private void validateProject(ProyectosFormularioDTO dto, Long currentId) {
        if (!StringUtils.hasText(dto.nombre())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El nombre del proyecto es obligatorio");
        }
        if (dto.idTipoActividad() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de actividad es obligatorio");
        }
        if (!tipoActividadesRepository.existsById(dto.idTipoActividad())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de actividad seleccionado no existe");
        }
        if (dto.idCoordinacion() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación / centro de costo es obligatorio");
        }
        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación seleccionada no existe");
        }
        if (dto.idProyectoPadre() != null) {
            if (currentId != null && dto.idProyectoPadre().equals(currentId)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Un proyecto no puede ser padre de sí mismo");
            }
            if (!proyectosRepository.existsById(dto.idProyectoPadre())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El proyecto padre no existe");
            }
        }
    }

    private void validateParticipant(PersonaProyectoFormularioDTO dto) {
        if (dto.idProyecto() == null || !proyectosRepository.existsById(dto.idProyecto())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El proyecto especificado no existe");
        }
        if (dto.idPersonaGeneral() == null || !personaGeneralRepository.existsById(dto.idPersonaGeneral())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El docente especificado no existe");
        }
    }

    private void mapDtoToEntity(ProyectosFormularioDTO dto, ProyectosEntity entity) {
        entity.setNombre(dto.nombre().trim());
        entity.setDescripcion(dto.descripcion() != null ? dto.descripcion().trim() : null);
        entity.setActaConsejo(dto.actaConsejo() != null ? dto.actaConsejo().trim() : null);
        entity.setIdTipoActividad(dto.idTipoActividad());
        entity.setIdCoordinacion(dto.idCoordinacion());
        entity.setIdProyectoPadre(dto.idProyectoPadre());
        entity.setIdMateriaTdg(dto.idMateriaTdg());
    }
}
