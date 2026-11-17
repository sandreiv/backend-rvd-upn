/**
 * Aplicación: rvd
 * Archivo: PensumDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Pensum del programa en el detalle de carga.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PensumDTO(
        Long id,
        String descripcion,
        Long anioInicio,
        String periodoInicio,
        Long numPeriodos
) {}
