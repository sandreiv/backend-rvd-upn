/**
 * Aplicación: rvd
 * Archivo: TiposDocumentoContratacionListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/11/2026
 * Modificaciones:
 * 27/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TiposDocumentoContratacionListadoDTO(
    Long id,
    String nombre,
    String descripcion,
    String esObligatorio
) {}
