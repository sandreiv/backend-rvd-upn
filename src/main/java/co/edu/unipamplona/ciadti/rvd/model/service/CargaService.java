/**
 * Aplicación: rvd
 * Archivo: CargaService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 05/11/2026 - Forma de pago desde el tipo de vinculación.
 * 05/11/2026 - Búsqueda de docente con valor del punto y asignación salarial.
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;


import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.BusquedaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosCargaDTO;


public interface CargaService {

    List<CoordinacionDTO> findCoordinationsByPerson(Long idConvocatoria, Long idPeriodoUniversidad);

    TipoVinculacionListadoDTO findLinkageTypesByCall(Long idConvocatoria);

    List<BusquedaDocenteDTO> searchProfessor(String nombre, String documento, Long anio);

    ValorPuntosCargaDTO getValuePointsPreload(
            Long anio,
            Long idCategoriaCatedratico,
            Long idPersona,
            Long idModalidadContratacion,
            Long idTipoVinculacion);


    
}
