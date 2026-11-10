/**
 * Aplicación: rvd
 * Archivo: TipoActividadesAdministracionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 10/11/2026 - Leonel Antonio Perez Rios - Adición de método para listar todos los tipos en jerarquía
 */

package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadAdministracionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadAdministracionListadoDTO;

public interface TipoActividadesAdministracionService {

    List<TipoActividadAdministracionListadoDTO> listParentActivityTypes();

    List<TipoActividadAdministracionListadoDTO> listAllActivityTypes();

    List<TipoActividadAdministracionListadoDTO> listChildActivityTypes(Long idPadre);

    void saveActivityType(TipoActividadAdministracionFormularioDTO dto);

    void saveChildActivityType(Long idPadre, TipoActividadAdministracionFormularioDTO dto);

    void updateActivityType(Long id, TipoActividadAdministracionFormularioDTO dto);

    void deleteActivityType(Long id);

    void deleteBulkActivityTypes(List<Long> ids);
}

/* 17/07/2026 @:Daniel Arias */
