/**
 * Aplicación: rvd
 * Archivo: VinculacionActividadAdministracionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Juan Diego Florez Vera - Creación inicial para administración de tablas básicas
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.OpcionAgrupadaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.VinculacionActividadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.VinculacionActividadListadoDTO;

public interface VinculacionActividadAdministracionService {

    List<VinculacionActividadListadoDTO> listVinculacionActividades();

    List<OpcionAgrupadaDTO> listTiposVinculacion();

    List<OpcionAgrupadaDTO> listTiposActividad();

    void saveVinculacionActividad(VinculacionActividadFormularioDTO dto);

    void updateVinculacionActividad(Long id, VinculacionActividadFormularioDTO dto);

    void deleteVinculacionActividad(Long id);

    void deleteBulkVinculacionActividades(List<Long> ids);
}
