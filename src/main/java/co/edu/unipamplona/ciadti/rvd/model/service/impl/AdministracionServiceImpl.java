/**
 * Aplicación: rvd
 * Archivo: AdministracionServiceImpl.java
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
import co.edu.unipamplona.ciadti.rvd.model.dto.AsociacionCoordinacionCatalogosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaCoordinacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaCoordinacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CatalogoAdministracionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.CentroCostoCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CentroCostoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProgramaCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProgramaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.AdministracionService;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoAsignadoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoAsignadoListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CentroCostoCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ProgramaCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdministracionServiceImpl implements AdministracionService {

    private final CoordinacionRepository coordinacionRepository;
    private final ProgramaRepository programaRepository;
    private final CentroCostoRepository centroCostoRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final PersonaCoordinacionRepository personaCoordinacionRepository;
    private final ProgramaCoordinacionRepository programaCoordinacionRepository;
    private final CentroCostoCoordinacionRepository centroCostoCoordinacionRepository;

    @Override
    @Transactional(readOnly = true)
    public AsociacionCoordinacionCatalogosDTO getAssociationCatalogs() {
        log.debug("getAssociationCatalogs ===> Consultando catálogos de administración");
        
        AsociacionCoordinacionCatalogosDTO result = new AsociacionCoordinacionCatalogosDTO(
            mapCatalog(coordinacionRepository.findAdministrationOptions()),
            mapCatalog(programaRepository.findAdministrationOptions()),
            mapCatalog(centroCostoRepository.findAdministrationOptions()),
            mapCatalog(personaGeneralRepository.findAdministrationOptions())
        );

        log.info("getAssociationCatalogs ===> Catálogos de administración consultados");
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramaCoordinacionListadoDTO> listCoordinationAssociations() {
        log.debug("listCoordinationAssociations ===> Listando asociaciones de coordinación");
        
        List<ProgramaCoordinacionListadoDTO> result = programaCoordinacionRepository.findAdministrationList()
            .stream()
            .map(item -> new ProgramaCoordinacionListadoDTO(
                item.getId(),
                item.getIdCoordinacion(),
                item.getCoordinacion(),
                item.getIdPrograma(),
                item.getPrograma(),
                item.getIdCentroCosto(),
                item.getCentroCosto(),
                item.getEstado()
            ))
            .toList();

        log.info("listCoordinationAssociations ===> Asociaciones de coordinación listadas. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveCoordinationAssociation(ProgramaCoordinacionFormularioDTO dto) {
        log.info("saveCoordinationAssociation ===> Guardando asociación de coordinación. idCoordinacion={}, idPrograma={}, idCentroCosto={}",
            dto != null ? dto.idCoordinacion() : null,
            dto != null ? dto.idPrograma() : null,
            dto != null ? dto.idCentroCosto() : null);

        validateAssociation(dto);

        ProgramaCoordinacionEntity association = new ProgramaCoordinacionEntity();
        fillAssociation(association, dto);
        ProgramaCoordinacionEntity saved = programaCoordinacionRepository.save(association);

        log.info("saveCoordinationAssociation ===> Asociación de coordinación guardada. id={}", saved.getId());
    }

    @Override
    @Transactional
    public void updateCoordinationAssociation(Long id, ProgramaCoordinacionFormularioDTO dto) {
        log.info("updateCoordinationAssociation ===> Actualizando asociación de coordinación. id={}, idCoordinacion={}, idPrograma={}, idCentroCosto={}",
            id,
            dto != null ? dto.idCoordinacion() : null,
            dto != null ? dto.idPrograma() : null,
            dto != null ? dto.idCentroCosto() : null);

        validateAssociation(dto);

        ProgramaCoordinacionEntity association = programaCoordinacionRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("updateCoordinationAssociation ===> Asociación de coordinación no encontrada. id={}", id);
                return new ApiException(HttpStatus.NOT_FOUND, "No existe la asociación con id " + id);
            });

        fillAssociation(association, dto);
        programaCoordinacionRepository.save(association);

        log.info("updateCoordinationAssociation ===> Asociación de coordinación actualizada. id={}", id);
    }

    @Override
    @Transactional
    public void deleteCoordinationAssociation(Long id) {
        /*
        log.info("deleteCoordinationAssociation ===> Eliminando asociación de coordinación. id={}", id);

        if (!programaCoordinacionRepository.existsById(id)) {
            log.warn("deleteCoordinationAssociation ===> Asociación de coordinación no encontrada. id={}", id);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la asociación con id " + id);
        }

        BigDecimal result = programaCoordinacionRepository.deleteByProcedure(
                id,
                RegistradoPorUtils.value(Accion.DELETE)
        );

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deleteCoordinationAssociation ===> Procedimiento de eliminación falló. id={}, resultado={}",
                    id, result);
        }

        validateProcedureResult(result, "No se pudo eliminar la asociación coordinación");

        log.info("deleteCoordinationAssociation ===> Asociación de coordinación eliminada. id={}", id);
        */
    }

    @Override
    @Transactional
    public void deleteBulkCoordinationAssociations(List<Long> ids) {
        /*
        log.info("deleteBulkCoordinationAssociations ===> Eliminación masiva de asociaciones de coordinación. total={}",
                ids != null ? ids.size() : 0);

        if (ids == null || ids.isEmpty()) {
            log.debug("deleteBulkCoordinationAssociations ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (Long id : ids) {
            deleteCoordinationAssociation(id);
        }

        log.info("deleteBulkCoordinationAssociations ===> Eliminación masiva de asociaciones finalizada. total={}",
                ids.size());
        */
    }

    private void fillAssociation(ProgramaCoordinacionEntity association, ProgramaCoordinacionFormularioDTO dto) {
        boolean hasProgram = dto.idPrograma() != null;

        association.setIdCoordinacion(dto.idCoordinacion());
        association.setIdPrograma(hasProgram ? dto.idPrograma() : null);
        association.setIdCentroCosto(hasProgram ? dto.idCentroCosto() : null);

        association.setEstado(normalizeStatus(dto.estado()));

        association.setRegistradoPor(
            RegistradoPorUtils.value(
                association.getId() == null
                    ? Accion.INSERT
                    : Accion.UPDATE
            )
        );

        association.setFechaCambio(new Date());
    }

    private void validateAssociation(ProgramaCoordinacionFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información de la asociación es obligatoria");
        }

        if (dto.idCoordinacion() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación es obligatoria");
        }

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La coordinación no existe");
        }

        if (dto.idPrograma() != null && !programaRepository.existsById(dto.idPrograma())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "El programa no existe");
        }

        if (dto.idCentroCosto() != null && !centroCostoRepository.existsById(dto.idCentroCosto())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "El centro de costo no existe");
        }
    }

    private String normalizeStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return "1";
        }

        String normalized = status.trim().toUpperCase();

        return "0".equals(normalized) || "INACTIVO".equals(normalized) || "I".equals(normalized) ? "0" : "1";
    }

    private List<CatalogoAdministracionDTO> mapCatalog(List<CatalogoAdministracionProjection> projections) {
        return projections
            .stream()
            .map(item -> new CatalogoAdministracionDTO(item.getId(), item.getLabel(), item.getCodigo()))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CentroCostoAsignadoListadoDTO> listCostCenterAssignments() {
        log.debug("listCostCenterAssignments ===> Listando centros de costo asignados");
        
        List<CentroCostoAsignadoListadoDTO> result = centroCostoCoordinacionRepository.findAdministrationList()
            .stream()
            .map(item -> new CentroCostoAsignadoListadoDTO(
                item.getId(),
                item.getIdCoordinacion(),
                item.getCoordinacion(),
                item.getIdCentroCosto(),
                item.getCentroCosto(),
                item.getEstado()
            ))
            .toList();

        log.info("listCostCenterAssignments ===> Centros de costo asignados listados. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveCostCenterAssignment(CentroCostoAsignadoFormularioDTO dto) {
        log.info("saveCostCenterAssignment ===> Guardando centro de costo asignado. idCoordinacion={}, idCentroCosto={}",
                dto != null ? dto.idCoordinacion() : null,
                dto != null ? dto.idCentroCosto() : null);

        validateCostCenterAssignment(dto);

        CentroCostoCoordinacionEntity entity = new CentroCostoCoordinacionEntity();
        fillCostCenterAssignment(entity, dto);
        CentroCostoCoordinacionEntity saved = centroCostoCoordinacionRepository.save(entity);

        log.info("saveCostCenterAssignment ===> Centro de costo asignado guardado. id={}", saved.getId());
    }

    @Override
    @Transactional
    public void updateCostCenterAssignment(Long id, CentroCostoAsignadoFormularioDTO dto) {
        log.info("updateCostCenterAssignment ===> Actualizando centro de costo asignado. id={}, idCoordinacion={}, idCentroCosto={}",
                id,
                dto != null ? dto.idCoordinacion() : null,
                dto != null ? dto.idCentroCosto() : null);

        validateCostCenterAssignment(dto);

        CentroCostoCoordinacionEntity entity = centroCostoCoordinacionRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("updateCostCenterAssignment ===> Centro de costo asignado no encontrado. id={}", id);
                return new ApiException(HttpStatus.NOT_FOUND, "No existe el centro de costo asignado con id " + id);
            });

        fillCostCenterAssignment(entity, dto);
        centroCostoCoordinacionRepository.save(entity);

        log.info("updateCostCenterAssignment ===> Centro de costo asignado actualizado. id={}", id);
    }

    @Override
    @Transactional
    public void deleteCostCenterAssignment(Long id) {
        /*
        log.info("deleteCostCenterAssignment ===> Eliminando centro de costo asignado. id={}", id);

        if (!asignarCentroCostoRepository.existsById(id)) {
            log.warn("deleteCostCenterAssignment ===> Centro de costo asignado no encontrado. id={}", id);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el centro de costo asignado con id " + id);
        }

        BigDecimal result = asignarCentroCostoRepository.deleteByProcedure(
                id,
                RegistradoPorUtils.value(Accion.DELETE)
        );

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deleteCostCenterAssignment ===> Procedimiento de eliminación falló. id={}, resultado={}",
                    id, result);
        }

        validateProcedureResult(result, "No se pudo eliminar el centro de costo asignado");

        log.info("deleteCostCenterAssignment ===> Centro de costo asignado eliminado. id={}", id);
        */
    }

    @Override
    @Transactional
    public void deleteBulkCostCenterAssignments(List<Long> ids) {
        /*
        log.info("deleteBulkCostCenterAssignments ===> Eliminación masiva de centros de costo asignados. total={}",
                ids != null ? ids.size() : 0);

        if (ids == null || ids.isEmpty()) {
            log.debug("deleteBulkCostCenterAssignments ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (Long id : ids) {
            deleteCostCenterAssignment(id);
        }

        log.info("deleteBulkCostCenterAssignments ===> Eliminación masiva de centros de costo finalizada. total={}",
                ids.size());
        */
    }

    private void fillCostCenterAssignment(CentroCostoCoordinacionEntity entity, CentroCostoAsignadoFormularioDTO dto) {
        entity.setIdCoordinacion(dto.idCoordinacion());
        entity.setIdCentroCosto(dto.idCentroCosto());
        entity.setEstado(normalizeStatus(dto.estado()));

        entity.setRegistradoPor(
            RegistradoPorUtils.value(
                entity.getId() == null
                    ? Accion.INSERT
                    : Accion.UPDATE
            )
        );

        entity.setFechaCambio(new Date());
    }

    private void validateCostCenterAssignment(CentroCostoAsignadoFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información del centro de costo es obligatoria");
        }

        if (dto.idCoordinacion() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación es obligatoria");
        }

        if (dto.idCentroCosto() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El centro de costo es obligatorio");
        }

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La coordinación no existe");
        }

        if (!centroCostoRepository.existsById(dto.idCentroCosto())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "El centro de costo no existe");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaCoordinacionListadoDTO> listPeopleCoordinations() {
        log.debug("listPeopleCoordinations ===> Listando personas asociadas a coordinaciones");

        List<PersonaCoordinacionListadoDTO> result = personaCoordinacionRepository.findAdministrationList()
            .stream()
            .map(item -> new PersonaCoordinacionListadoDTO(
                item.getId(),
                item.getIdPersonaGeneral(),
                item.getPersona(),
                item.getDocumentoIdentidad(),
                item.getIdCoordinacion(),
                item.getCoordinacion(),
                item.getEstado()
            ))
            .toList();

        log.info("listPeopleCoordinations ===> Personas asociadas a coordinaciones listadas. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void savePeopleCoordination(PersonaCoordinacionFormularioDTO dto) {
        log.info("savePeopleCoordination ===> Guardando persona asociada a coordinación. idPersonaGeneral={}, idCoordinacion={}",
            dto != null ? dto.idPersonaGeneral() : null,
            dto != null ? dto.idCoordinacion() : null);

        validatePersonCoordination(dto);

        PersonaCoordinacionEntity entity = new PersonaCoordinacionEntity();
        fillPeopleCoordination(entity, dto);
        PersonaCoordinacionEntity saved = personaCoordinacionRepository.save(entity);

        log.info("savePeopleCoordination ===> Persona asociada a coordinación guardada. id={}", saved.getId());
    }

    @Override
    @Transactional
    public void updatePeopleCoordination(Long id, PersonaCoordinacionFormularioDTO dto) {
        log.info("updatePeopleCoordination ===> Actualizando persona asignada. id={}, idCoordinacion={}, idPersonaGeneral={}",
            id,
            dto != null ? dto.idCoordinacion() : null,
            dto != null ? dto.idPersonaGeneral() : null);

        validatePersonCoordination(dto);

        PersonaCoordinacionEntity entity = personaCoordinacionRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("updatePeopleCoordination ===> Persona asignada no encontrada. id={}", id);
                return new ApiException(HttpStatus.NOT_FOUND, "No existe la persona asignada con id " + id);
            });
        
        fillPeopleCoordination(entity, dto);
        personaCoordinacionRepository.save(entity);

        log.info("updatePeopleCoordination ===> Persona asociada a coordinación actualizada. id={}", id);
    }

    @Override
    @Transactional
    public void deletePeopleCoordination(Long idPersonaGeneral, Long idCoordinacion) {
        /*
        log.info("deletePeopleCoordination ===> Eliminando persona asociada a coordinación. idPersonaGeneral={}, idCoordinacion={}",
                idPersonaGeneral, idCoordinacion);

        PersonaCoordinacionEntityId id = personaCoordinationId(idPersonaGeneral, idCoordinacion);

        if (!personaCoordinacionRepository.existsById(id)) {
            log.warn("deletePeopleCoordination ===> Persona asociada a coordinación no encontrada. idPersonaGeneral={}, idCoordinacion={}",
                    idPersonaGeneral, idCoordinacion);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la persona asociada a la coordinación");
        }

        BigDecimal result = personaCoordinacionRepository.deleteByProcedure(
                idPersonaGeneral,
                idCoordinacion,
                RegistradoPorUtils.value(Accion.DELETE)
        );

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deletePeopleCoordination ===> Procedimiento de eliminación falló. idPersonaGeneral={}, idCoordinacion={}, resultado={}",
                    idPersonaGeneral, idCoordinacion, result);
        }

        validateProcedureResult(result, "No se pudo eliminar la persona asociada a la coordinación");

        log.info("deletePeopleCoordination ===> Persona asociada a coordinación eliminada. idPersonaGeneral={}, idCoordinacion={}",
                idPersonaGeneral, idCoordinacion);
        */
    }

    @Override
    @Transactional
    public void deleteBulkPeopleCoordinations(List<Long> ids) {
        /*
        log.info("deleteBulkPeopleCoordinations ===> Eliminación masiva de personas asociadas a coordinación. total={}",
                registros != null ? registros.size() : 0);

        if (registros == null || registros.isEmpty()) {
            log.debug("deleteBulkPeopleCoordinations ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (PersonaCoordinacionClaveDTO item : registros) {
            deletePeopleCoordination(item.idPersonaGeneral(), item.idCoordinacion());
        }

        log.info("deleteBulkPeopleCoordinations ===> Eliminación masiva de personas asociadas finalizada. total={}",
                registros.size());
        */
    }

    private void fillPeopleCoordination(PersonaCoordinacionEntity entity, PersonaCoordinacionFormularioDTO dto) {
        entity.setIdPersonaGeneral(dto.idPersonaGeneral());
        entity.setIdCoordinacion(dto.idCoordinacion());
        entity.setEstado(normalizeStatus(dto.estado()));
        
        entity.setRegistradoPor(
            RegistradoPorUtils.value(
                entity.getId() == null
                    ? Accion.INSERT
                    : Accion.UPDATE
            )
        );
        entity.setFechaCambio(new Date());
    }

    private void validatePersonCoordination(PersonaCoordinacionFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información es obligatoria");
        }

        if (dto.idPersonaGeneral() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La persona es obligatoria");
        }

        if (dto.idCoordinacion() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación es obligatoria");
        }

        if (!personaGeneralRepository.existsById(dto.idPersonaGeneral())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La persona no existe");
        }

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La coordinación no existe");
        }
    }
}
