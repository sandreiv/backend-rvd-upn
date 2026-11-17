/**
 * Aplicación: rvd
 * Archivo: DiaSemanaOpcionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - DTO de opciones para días de la semana
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record DiaSemanaOpcionDTO(
        String codigo,
        String nombre,
        String abreviacion
) {}
