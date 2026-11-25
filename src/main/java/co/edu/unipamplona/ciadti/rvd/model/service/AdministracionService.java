/**
 * Aplicación: rvd
 * Archivo: AdministracionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.AsociacionCoordinacionCatalogosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaCoordinacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaCoordinacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoAsignadoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoAsignadoListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionListadoDTO;

public interface AdministracionService {

    AsociacionCoordinacionCatalogosDTO getAssociationCatalogs();

    List<ProgramaCoordinacionListadoDTO> listCoordinationAssociations();

    void saveCoordinationAssociation(ProgramaCoordinacionFormularioDTO dto);

    void updateCoordinationAssociation(Long id, ProgramaCoordinacionFormularioDTO dto);

    void deleteCoordinationAssociation(Long id);

    void deleteBulkCoordinationAssociations(List<Long> ids);

    List<CentroCostoAsignadoListadoDTO> listCostCenterAssignments();

    void saveCostCenterAssignment(CentroCostoAsignadoFormularioDTO dto);

    void updateCostCenterAssignment(Long id, CentroCostoAsignadoFormularioDTO dto);

    void deleteCostCenterAssignment(Long id);

    void deleteBulkCostCenterAssignments(List<Long> ids);

    List<PersonaCoordinacionListadoDTO> listPeopleCoordinations();

    void savePeopleCoordination(PersonaCoordinacionFormularioDTO dto);

    void updatePeopleCoordination(Long id, PersonaCoordinacionFormularioDTO dto);

    void deletePeopleCoordination(Long id);

    void deleteBulkPeopleCoordinations(List<Long> ids);
}
