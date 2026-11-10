/**
 * Aplicación: rvd
 * Archivo: ProyectosService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Leonel Antonio Perez Rios - Creación inicial
 * 10/11/2026 - Leonel Antonio Perez Rios - Búsqueda de personas para proyectos
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadProyectoDTO;

public interface ProyectosService {

    List<ProyectosListaDTO> listProjects();

    List<ProyectosListaDTO> listProducts(Long idProyecto);

    List<TipoActividadProyectoDTO> listProjectActivityTypes();

    List<CoordinacionOpcionDTO> searchCoordinations(String term);

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
}
