/**
 * Aplicación: rvd
 * Archivo: PensumListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PensumListadoDTO(
        Long id,
        String descripcion,
        Long anioInicio,
        String periodoInicio,
        Long numPeriodos
) {}
