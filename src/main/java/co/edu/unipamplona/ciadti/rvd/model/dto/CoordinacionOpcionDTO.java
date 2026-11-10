/**
 * Aplicación: rvd
 * Archivo: CoordinacionOpcionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/11/2026
 * Modificaciones:
 * 10/11/2026 - Leonel Antonio Perez Rios - DTO para opciones de búsqueda de coordinaciones
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionOpcionDTO(
        Long id,
        String nombre,
        String descripcion
) {}
