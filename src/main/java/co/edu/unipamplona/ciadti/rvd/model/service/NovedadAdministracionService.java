/**
 * Aplicación: rvd
 * Archivo: NovedadAdministracionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 24/11/2026
 * Modificaciones:
 * 24/11/2026 - Creación inicial para administración de tablas básicas (NOVEDADES)
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadListadoDTO;

public interface NovedadAdministracionService {

    List<NovedadListadoDTO> listNovedades();

    void saveNovedad(NovedadFormularioDTO dto);

    void updateNovedad(Long id, NovedadFormularioDTO dto);

    void deleteNovedad(Long id);

    void deleteBulkNovedades(List<Long> ids);
}
