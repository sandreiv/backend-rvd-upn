/**
 * Aplicación: rvd
 * Archivo: TipoActividadComponenteDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - DTO de configuración de requisitos de participantes por componente de actividad
 * 20/11/2026 - Inclusión de aplicaPrograma
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadComponenteDTO(
        String codigo,
        String descripcion,
        boolean requiereFuncion,
        boolean requiereNivel,
        boolean requiereHorario,
        boolean aplicaPrograma
) {}