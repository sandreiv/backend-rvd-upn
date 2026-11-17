/**
 * Aplicación: rvd
 * Archivo: ProyectoGrupoService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoOpcionDTO;

public interface ProyectoGrupoService {

    List<ProyectoGrupoListaDTO> listProjectGroups();

    ProyectoGrupoDTO getProjectGroupById(Long id);

    void saveProjectGroup(ProyectoGrupoFormularioDTO dto);

    void updateProjectGroup(Long id, ProyectoGrupoFormularioDTO dto);

    void deleteProjectGroup(Long id);

    List<ProyectoGrupoOpcionDTO> listProjectGroupOptions();

    List<ConvocatoriaOpcionDTO> listAvailableConvocatorias();
}
