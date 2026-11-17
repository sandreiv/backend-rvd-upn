/**
 * Aplicación: rvd
 * Archivo: TipoActividadesService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 10/11/2026 - Leonel Antonio Perez Rios - Adición de método para listar todos los tipos en jerarquía
 */

package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDetalleDTO;

public interface TipoActividadesService {

    List<TipoActividadDetalleDTO> listParentActivityTypes();

    List<TipoActividadDetalleDTO> listAllActivityTypes();

    List<TipoActividadDetalleDTO> listChildActivityTypes(Long idPadre);

    void saveActivityType(TipoActividadFormularioDTO dto);

    void saveChildActivityType(Long idPadre, TipoActividadFormularioDTO dto);

    void updateActivityType(Long id, TipoActividadFormularioDTO dto);

    void deleteActivityType(Long id);

    void deleteBulkActivityTypes(List<Long> ids);
}

/* 17/07/2026 @:Daniel Arias */
