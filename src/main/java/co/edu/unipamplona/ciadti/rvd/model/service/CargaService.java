/**
 * Aplicación: rvd
 * Archivo: CargaService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;


import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionListadoDTO;


public interface CargaService {

    List<CoordinacionDTO> findCoordinationsByPerson(Long idConvocatoria, Long idPeriodoUniversidad);

    TipoVinculacionListadoDTO findLinkageTypesByCall(Long idConvocatoria);


    
}
