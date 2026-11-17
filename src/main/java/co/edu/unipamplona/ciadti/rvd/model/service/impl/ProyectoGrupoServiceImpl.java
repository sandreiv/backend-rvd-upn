/**
 * Aplicación: rvd
 * Archivo: ProyectoGrupoServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.GrupoConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ProyectoGrupoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.GrupoConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProyectoGrupoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProyectoGrupoListaProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.ProyectoGrupoService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProyectoGrupoServiceImpl implements ProyectoGrupoService {

    private final ProyectoGrupoRepository proyectoGrupoRepository;
    private final GrupoConvocatoriaRepository grupoConvocatoriaRepository;
    private final ConvocatoriaRepository convocatoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoGrupoListaDTO> listProjectGroups() {
        List<ProyectoGrupoListaProjection> projections = proyectoGrupoRepository.findAllProjectGroupsList();
        if (projections.isEmpty()) {
            return Collections.emptyList();
        }

        List<ProyectoGrupoListaDTO> result = new ArrayList<>(projections.size());
        for (ProyectoGrupoListaProjection p : projections) {
            List<GrupoConvocatoriaEntity> convocatoriasGrupo = grupoConvocatoriaRepository.findByIdProyectoGrupoWithConvocatoria(p.getId());
            List<ConvocatoriaOpcionDTO> convocatoriasDTO = convocatoriasGrupo.stream()
                    .map(gc -> mapConvocatoriaToOption(gc.getConvocatoria()))
                    .filter(c -> c != null)
                    .toList();

            result.add(new ProyectoGrupoListaDTO(
                    p.getId(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getEstado(),
                    p.getTotalProyectos(),
                    p.getTotalConvocatorias(),
                    convocatoriasDTO
            ));
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoGrupoDTO getProjectGroupById(Long id) {
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador del grupo es obligatorio");
        }

        ProyectoGrupoEntity entity = proyectoGrupoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Grupo de proyectos no encontrado"));

        List<GrupoConvocatoriaEntity> convocatoriasGrupo = grupoConvocatoriaRepository.findByIdProyectoGrupoWithConvocatoria(id);
        List<Long> idsConvocatoria = convocatoriasGrupo.stream()
                .map(GrupoConvocatoriaEntity::getIdConvocatoria)
                .toList();

        List<ConvocatoriaOpcionDTO> convocatoriasDTO = convocatoriasGrupo.stream()
                .map(gc -> mapConvocatoriaToOption(gc.getConvocatoria()))
                .filter(c -> c != null)
                .toList();

        return new ProyectoGrupoDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getEstado(),
                idsConvocatoria,
                convocatoriasDTO
        );
    }

    @Override
    @Transactional
    public void saveProjectGroup(ProyectoGrupoFormularioDTO dto) {
        validateForm(dto, null);

        ProyectoGrupoEntity entity = new ProyectoGrupoEntity();
        entity.setNombre(dto.nombre().trim());
        entity.setDescripcion(StringUtils.hasText(dto.descripcion()) ? dto.descripcion().trim() : null);
        entity.setEstado(StringUtils.hasText(dto.estado()) ? dto.estado().trim() : "1");
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        entity.setFechaCambio(new Date());

        ProyectoGrupoEntity saved = proyectoGrupoRepository.save(entity);

        syncConvocatorias(saved.getId(), dto.idConvocatorias(), Accion.INSERT);
    }

    @Override
    @Transactional
    public void updateProjectGroup(Long id, ProyectoGrupoFormularioDTO dto) {
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador del grupo es obligatorio");
        }

        ProyectoGrupoEntity entity = proyectoGrupoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Grupo de proyectos no encontrado"));

        validateForm(dto, id);

        entity.setNombre(dto.nombre().trim());
        entity.setDescripcion(StringUtils.hasText(dto.descripcion()) ? dto.descripcion().trim() : null);
        entity.setEstado(StringUtils.hasText(dto.estado()) ? dto.estado().trim() : entity.getEstado());
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        entity.setFechaCambio(new Date());

        proyectoGrupoRepository.save(entity);

        syncConvocatorias(id, dto.idConvocatorias(), Accion.UPDATE);
    }

    @Override
    @Transactional
    public void deleteProjectGroup(Long id) {
        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador del grupo es obligatorio");
        }

        ProyectoGrupoEntity entity = proyectoGrupoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Grupo de proyectos no encontrado"));

        if (entity.getProyectos() != null && !entity.getProyectos().isEmpty()) {
            throw new ApiException(HttpStatus.CONFLICT, "No se puede eliminar el grupo porque tiene proyectos asociados. Cambie su estado a inactivo si no desea utilizarlo.");
        }

        grupoConvocatoriaRepository.deleteByIdProyectoGrupo(id);
        proyectoGrupoRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoGrupoOpcionDTO> listProjectGroupOptions() {
        return proyectoGrupoRepository.findAllActiveOrderByNombre().stream()
                .map(pg -> new ProyectoGrupoOpcionDTO(pg.getId(), pg.getNombre(), pg.getDescripcion()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConvocatoriaOpcionDTO> listAvailableConvocatorias() {
        List<ConvocatoriaEntity> convocatorias = convocatoriaRepository.findAllByOrderByIdDesc();
        return convocatorias.stream()
                .map(this::mapConvocatoriaToOption)
                .filter(c -> c != null)
                .toList();
    }

    private void validateForm(ProyectoGrupoFormularioDTO dto, Long idExistente) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información del grupo es obligatoria");
        }

        if (!StringUtils.hasText(dto.nombre())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El nombre del grupo es obligatorio");
        }

        boolean existeNombre = idExistente == null
                ? proyectoGrupoRepository.existsByNombreIgnoreCase(dto.nombre().trim())
                : proyectoGrupoRepository.existsByNombreIgnoreCaseAndIdNot(dto.nombre().trim(), idExistente);

        if (existeNombre) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un grupo de proyectos con el nombre ingresado");
        }
    }

    private void syncConvocatorias(Long idProyectoGrupo, List<Long> nuevosIds, Accion accion) {
        List<GrupoConvocatoriaEntity> actuales = grupoConvocatoriaRepository.findByIdProyectoGrupo(idProyectoGrupo);
        Set<Long> nuevosSet = nuevosIds != null ? new HashSet<>(nuevosIds) : Collections.emptySet();

        Map<Long, GrupoConvocatoriaEntity> actualesMap = actuales.stream()
                .collect(Collectors.toMap(GrupoConvocatoriaEntity::getIdConvocatoria, g -> g));

        // Eliminar las que ya no están seleccionadas
        for (GrupoConvocatoriaEntity actual : actuales) {
            if (!nuevosSet.contains(actual.getIdConvocatoria())) {
                grupoConvocatoriaRepository.delete(actual);
            }
        }

        // Agregar las nuevas
        for (Long idConv : nuevosSet) {
            if (idConv != null && !actualesMap.containsKey(idConv)) {
                if (!convocatoriaRepository.existsById(idConv)) {
                    continue;
                }
                GrupoConvocatoriaEntity nuevoDetalle = new GrupoConvocatoriaEntity();
                nuevoDetalle.setIdProyectoGrupo(idProyectoGrupo);
                nuevoDetalle.setIdConvocatoria(idConv);
                nuevoDetalle.setEstado("1");
                nuevoDetalle.setRegistradoPor(RegistradoPorUtils.value(accion));
                nuevoDetalle.setFechaCambio(new Date());
                grupoConvocatoriaRepository.save(nuevoDetalle);
            }
        }
    }

    private ConvocatoriaOpcionDTO mapConvocatoriaToOption(ConvocatoriaEntity c) {
        if (c == null) {
            return null;
        }

        String periodoStr = "";
        PeriodoUniversidadEntity pu = c.getPeriodoUniversidad();
        if (pu != null) {
            periodoStr = pu.getAno() != null ? pu.getAno().toString() : "";
            if (StringUtils.hasText(pu.getPeriodo())) {
                periodoStr += "-" + pu.getPeriodo();
            }
        }

        return new ConvocatoriaOpcionDTO(
                c.getId(),
                c.getNombre(),
                periodoStr,
                c.getEstado()
        );
    }
}
