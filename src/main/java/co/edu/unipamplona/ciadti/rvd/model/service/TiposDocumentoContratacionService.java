/**
 * Aplicación: rvd
 * Archivo: TiposDocumentoContratacionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/11/2026
 * Modificaciones:
 * 27/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.TiposDocumentoContratacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TiposDocumentoContratacionListadoDTO;

public interface TiposDocumentoContratacionService {

    List<TiposDocumentoContratacionListadoDTO> listDocumentTypes();

    void saveDocumentType(TiposDocumentoContratacionFormularioDTO dto);

    void updateDocumentType(Long id, TiposDocumentoContratacionFormularioDTO dto);

    void deleteDocumentType(Long id);

    void deleteBulkDocumentTypes(List<Long> ids);
}
