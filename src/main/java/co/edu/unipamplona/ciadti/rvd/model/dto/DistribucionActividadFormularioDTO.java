/**
 * Aplicación: rvd
 * Archivo: DistribucionActividadFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record DistribucionActividadFormularioDTO(
        Long idCargaDocente,
        List<DetalleCargaDocenteFormularioDTO> detalles
) {}
