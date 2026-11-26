/**
 * Aplicación: rvd
 * Archivo: OpcionAgrupadaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Juan Diego Florez Vera - Creación inicial: opción de catálogo con grupo para los selects
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

/**
 * Opción de catálogo para los selects del frontend.
 * {@code grupo} lo usa el componente app-select para agrupar por padre.
 */
public record OpcionAgrupadaDTO(
        Long id,
        String nombre,
        String grupo
) {}
