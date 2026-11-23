/**
 * Aplicación: rvd
 * Archivo: ProyectosService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Leonel Antonio Perez Rios - Creación inicial
 * 10/11/2026 - Leonel Antonio Perez Rios - Búsqueda de personas para proyectos
 * 10/11/2026 - Leonel Antonio Perez Rios - Soporte para listado completo jerárquico de proyectos y productos
 * 13/11/2026 - Inclusión de listado de centros de costo para participantes
 * 17/11/2026 - Inclusión de listado de funciones de participantes desde enum
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionCentrosCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionSupervisionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DiaSemanaOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstudianteBusquedaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstudianteProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstudianteProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FuncionProyectoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NivelProyectoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadComponenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadProyectoDTO;

public interface ProyectosService {

    List<ProyectosListaDTO> listProjects();

    default List<ProyectosListaDTO> listAllProjects() {
        return listAllProjects(null);
    }

    List<ProyectosListaDTO> listAllProjects(Long idCoordinacion);

    List<CoordinacionSupervisionDTO> listSupervisionCoordinations();

    List<ProyectosListaDTO> listProducts(Long idProyecto);

    List<TipoActividadProyectoDTO> listProjectActivityTypes();

    List<TipoActividadComponenteDTO> listActivityComponents();

    List<CoordinacionOpcionDTO> searchCoordinations(String term);

    default CoordinacionCentrosCostoDTO listCostCenters() {
        return listCostCenters(null, null);
    }

    CoordinacionCentrosCostoDTO listCostCenters(Long idProyecto, Long idCoordinacion);

    List<CentroCostoDTO> searchCostCenters(String term);

    List<ProyectoGrupoOpcionDTO> listProjectGroupOptions();

    List<FuncionProyectoOpcionDTO> listParticipantFunctions();

    List<NivelProyectoOpcionDTO> listParticipantLevels();

    List<DiaSemanaOpcionDTO> listDaysOfWeek();

    List<PersonaAutorizaConvocatoriaDTO> searchPersons(String term);

    void saveProject(ProyectosFormularioDTO dto);

    void updateProject(Long id, ProyectosFormularioDTO dto);

    void deleteProject(Long id);

    void deleteBulkProjects(List<Long> ids);

    List<PersonaProyectoListaDTO> listProjectParticipants(Long idProyecto);

    void saveProjectParticipant(PersonaProyectoFormularioDTO dto);

    void updateProjectParticipant(Long id, PersonaProyectoFormularioDTO dto);

    void deleteProjectParticipant(Long id);

    void deleteBulkProjectParticipants(List<Long> ids);

    List<EstudianteProyectoListaDTO> listProjectStudents(Long idProyecto);

    List<EstudianteBusquedaDTO> searchStudents(String term);

    void saveProjectStudent(EstudianteProyectoFormularioDTO dto);

    void deleteProjectStudent(Long id);

    void deleteBulkProjectStudents(List<Long> ids);
}
