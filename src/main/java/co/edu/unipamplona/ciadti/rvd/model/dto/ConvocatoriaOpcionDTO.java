/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaOpcionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record ConvocatoriaOpcionDTO(
    Long id,
    String nombre,
    String periodo,
    String estado
) {}
