/**
 * Aplicación: rvd
 * Archivo: TipoActividadProyectoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/11/2026
 * Modificaciones:
 * 10/11/2026 - Leonel Antonio Perez Rios - DTO para opciones jerárquicas de tipo de actividad
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadProyectoDTO(
        Long id,
        Long idPadre,
        String nombre,
        String nombrePadre,
        String nombreCompleto,
        String descripcion,
        String componente,
        Boolean esSeleccionable
) {}
