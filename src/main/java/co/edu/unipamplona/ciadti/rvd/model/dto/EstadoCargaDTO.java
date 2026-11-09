/**
 * Aplicación: rvd
 * Archivo: EstadoCargaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record EstadoCargaDTO(
        Long id,
        String nombre,
        String descripcion,
        String orden,
        String tipo
) {}
