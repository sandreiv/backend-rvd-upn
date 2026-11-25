/**
 * Aplicación: rvd
 * Archivo: CoordinacionAdministracionServiceImpl.java
 * Paquete: Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.CatalogoAdministracionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionAdministracionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionAdministracionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CoordinacionesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CentroCostoCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProgramaCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.UnidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionAdministracionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionAdministracionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoordinacionAdministracionServiceImpl implements CoordinacionAdministracionService {

    private final CoordinacionRepository coordinacionRepository;
    private final UnidadRepository unidadRepository;
    private final CentroCostoCoordinacionRepository centroCostoCoordinacionRepository;
    private final ProgramaCoordinacionRepository programaCoordinacionRepository;
    private final PersonaCoordinacionRepository personaCoordinacionRepository;

    private final AdministracionServiceImpl administracionServiceImpl;

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoAdministracionDTO> searchUnits(String term) {
        String normalizedTerm = StringUtils.hasText(term) ? term.trim() : null;

        log.debug("searchUnits ===> Buscando unidades para administración de coordinaciones. term={}",
            normalizedTerm);

        List<CatalogoAdministracionDTO> result = mapCatalog(unidadRepository.searchAdministrationUnits(normalizedTerm));

        log.info("searchUnits ===> Unidades encontradas. term={}, total={}",
            normalizedTerm, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionAdministracionListadoDTO> listParentCoordinations() {
        log.debug("listParentCoordinations ===> Listando coordinaciones padre");

        List<CoordinacionAdministracionListadoDTO> result = coordinacionRepository.findAdministrationParentCoordinations()
            .stream()
            .map(this::mapToDto)
            .toList();

        log.info("listParentCoordinations ===> Coordinaciones padre listadas. total={}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionAdministracionListadoDTO> listChildCoordinations(Long idPadre) {
        log.debug("listChildCoordinations ===> Listando coordinaciones hijas. idPadre={}", idPadre);

        if (idPadre == null || !coordinacionRepository.existsById(idPadre)) {
            log.warn("listChildCoordinations ===> Coordinación padre no encontrada. idPadre={}", idPadre);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinación padre");
        }

        List<CoordinacionAdministracionListadoDTO> result = coordinacionRepository.findAdministrationChildCoordinations(idPadre)
            .stream()
            .map(this::mapToDto)
            .toList();

        log.info("listChildCoordinations ===> Coordinaciones hijas listadas. idPadre={}, total={}",
            idPadre, result.size());

        return result;
    }

    @Override
    @Transactional
    public void saveParentCoordination(CoordinacionAdministracionFormularioDTO dto) {
        log.info("saveParentCoordination ===> Guardando coordinación padre. idUnidad={}",
            dto != null ? dto.idUnidad() : null);

        saveCoordinationWithParent(null, dto);

        log.info("saveParentCoordination ===> Coordinación padre guardada. idUnidad={}",
            dto != null ? dto.idUnidad() : null);
    }

    @Override
    @Transactional
    public void saveChildCoordination(Long idPadre, CoordinacionAdministracionFormularioDTO dto) {
        log.info("saveChildCoordination ===> Guardando coordinación hija. idPadre={}, idUnidad={}",
            idPadre,
            dto != null ? dto.idUnidad() : null);

        if (idPadre == null || !coordinacionRepository.existsById(idPadre)) {
            log.warn("saveChildCoordination ===> Coordinación padre no encontrada. idPadre={}", idPadre);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinación padre");
        }

        saveCoordinationWithParent(idPadre, dto);

        log.info("saveChildCoordination ===> Coordinación hija guardada. idPadre={}, idUnidad={}",
            idPadre,
            dto != null ? dto.idUnidad() : null);
    }

    @Override
    @Transactional
    public void updateCoordination(Long id, CoordinacionAdministracionFormularioDTO dto) {
        log.info("updateCoordination ===> Actualizando coordinación. id={}, idUnidad={}",
            id,
            dto != null ? dto.idUnidad() : null);

        validateCoordination(dto);

        CoordinacionesEntity entity = coordinacionRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("updateCoordination ===> Coordinación no encontrada. id={}", id);
                return new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinación con id " + id);
            });

        /*
        * No cambiamos COOR_IDPADRE en edición.
        * Si era padre, sigue padre.
        * Si era hija, sigue hija.
        */

        fillCoordination(entity, dto);

        CoordinacionesEntity saved = coordinacionRepository.save(entity);

        log.info("updateCoordination ===> Coordinación actualizada. id={}, idUnidad={}",
                saved.getId(), dto.idUnidad());
    }

    @Override
    @Transactional
    public void deleteCoordination(Long id) {
        log.info("deleteCoordination ===> Eliminando coordinación. id={}", id);

        CoordinacionesEntity entity = coordinacionRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("deleteCoordination ===> Coordinación no encontrada. id={}", id);
                return new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinación con id " + id);
            });

        if (coordinacionRepository.existsByIdCoordinacionPadre(entity.getId())) {
            log.warn("deleteCoordination ===> Eliminación bloqueada. La coordinación tiene hijas asociadas. id={}", entity.getId());
            throw new ApiException(HttpStatus.BAD_REQUEST, "No se puede eliminar la coordinación porque tiene coordinaciones hijas asociadas");
        }

        // Eliminar los programas asociados
        List<Long> programsAssignmentIds = programaCoordinacionRepository.findIdsByIdCoordinacion(id);
        log.debug("deleteCoordination ===> Asignaciones de programas encontradas para eliminar. idCoordinacion={}, total={}",
            id, programsAssignmentIds.size());

        for (Long assignmentId : programsAssignmentIds) {
            administracionServiceImpl.deleteCoordinationAssociation(id);

            log.info("deleteCoordination ===> Programa asignado eliminado. idCoordinacion={}, idAsignacion={}",
                id, assignmentId);
        }

        // Eliminar las personas asociadas
        List<Long> peopleAssignmentIds = personaCoordinacionRepository.findIdsByIdCoordinacion(id);
        log.debug("deleteCoordination ===> Asignaciones de personas encontradas para eliminar. idCoordinacion={}, total={}",
            id, peopleAssignmentIds.size());

        for (Long assignmentId : peopleAssignmentIds) {
            administracionServiceImpl.deletePeopleCoordination(id);

            log.info("deleteCoordination ===> Persona asignada eliminada. idCoordinacion={}, idAsignacion={}",
                id, assignmentId);
        }
        
        // Eliminar los centros de costo asociados
        List<Long> costCenterAssignmentIds = centroCostoCoordinacionRepository.findIdsByIdCoordinacion(id);
        log.debug("deleteCoordination ===> Asignaciones de centro de costo encontradas para eliminar. idCoordinacion={}, total={}",
            id, costCenterAssignmentIds.size());

        for (Long assignmentId : costCenterAssignmentIds) {
            administracionServiceImpl.deleteCostCenterAssignment(id);

            log.info("deleteCoordination ===> Centro de costo asignado eliminado. idCoordinacion={}, idAsignacion={}",
                id, assignmentId);
        }

        // Eliminar la coordinación
        coordinacionRepository.delete(entity);

        log.info("deleteCoordination ===> Coordinación eliminada. id={}", id);
    }

    @Override
    @Transactional
    public void deleteBulkCoordinations(List<Long> ids) {
        log.info("deleteBulkCoordinations ===> Eliminación masiva de coordinaciones. total={}",
            ids != null ? ids.size() : 0);

        if (ids == null || ids.isEmpty()) {
            log.debug("deleteBulkCoordinations ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (Long id : ids) {
            deleteCoordination(id);
        }

        log.info("deleteBulkCoordinations ===> Eliminación masiva de coordinaciones finalizada. total={}",
            ids.size());
    }

    
    private void saveCoordinationWithParent(Long idPadre, CoordinacionAdministracionFormularioDTO dto) {
        log.debug("saveCoordinationWithParent ===> Iniciando creación de coordinación. idPadre={}, idUnidad={}",
            idPadre,
            dto != null ? dto.idUnidad() : null);

        validateCoordination(dto);

        CoordinacionesEntity entity = new CoordinacionesEntity();
        
        entity.setIdCoordinacionPadre(idPadre);
        fillCoordination(entity, dto);
        
        CoordinacionesEntity saved = coordinacionRepository.save(entity);

        log.info("saveCoordinationWithParent ===> Coordinación creada. id={}, idPadre={}, idUnidad={}",
            saved.getId(),
            idPadre,
            saved.getIdUnidadArea());
    }

    private void fillCoordination(CoordinacionesEntity entity, CoordinacionAdministracionFormularioDTO dto) {
        entity.setIdUnidadRegional(dto.idUnidadRegional());
        entity.setIdUnidadArea(dto.idUnidad());
        entity.setEsAcademica(normalizeCheck(dto.esAcademica()));
        entity.setRegistradoPor(
            RegistradoPorUtils.value(
                entity.getId() == null
                    ? Accion.INSERT
                    : Accion.UPDATE
            )
        );
        entity.setFechaCambio(new Date());
    }

    private void validateCoordination(CoordinacionAdministracionFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información de la coordinación es obligatoria");
        }

        if (dto.idUnidadRegional() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La unidad regional es obligatoria");
        }

        if (dto.idUnidad() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La unidad es obligatoria");
        }

        if (!unidadRepository.existsById(dto.idUnidadRegional())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La unidad regional no existe");
        }

        if (!unidadRepository.existsById(dto.idUnidad())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La unidad no existe");
        }
    }

    private CoordinacionAdministracionListadoDTO mapToDto(CoordinacionAdministracionListadoProjection item) {
        return new CoordinacionAdministracionListadoDTO(
            item.getId(),
            item.getIdCoordinacionPadre(),
            item.getIdUnidadRegional(),
            item.getUnidadRegional(),
            item.getIdUnidad(),
            item.getUnidad(),
            item.getEsAcademica()
        );
    }

    private List<CatalogoAdministracionDTO> mapCatalog(List<CatalogoAdministracionProjection> items) {
        return items
            .stream()
            .map(item -> new CatalogoAdministracionDTO(
                item.getId(),
                item.getLabel(),
                item.getCodigo()
            ))
            .toList();
    }

    private String normalizeCheck(String value) {
        if (!StringUtils.hasText(value)) {
            return "0";
        }

        String normalized = value.trim().toUpperCase();

        return "1".equals(normalized)
            || "ACTIVO".equals(normalized)
            || "TRUE".equals(normalized)
            || "SI".equals(normalized)
            || "S".equals(normalized)
            ? "1"
            : "0";
    }
}
