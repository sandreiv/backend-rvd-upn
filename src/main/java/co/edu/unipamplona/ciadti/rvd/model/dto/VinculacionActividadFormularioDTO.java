/**
 * Aplicación: rvd
 * Archivo: VinculacionActividadFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Juan Diego Florez Vera - Creación inicial para administración de tablas básicas
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record VinculacionActividadFormularioDTO(
        Long idTipoVinculacion,
        Long idTipoActividad,
        String estado,
        String minimoHoras,
        String maximoHoras
) {}
