/**
 * Aplicación: rvd
 * Archivo: HorarioProyectoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - Bloque de horario guardado en PEPR_DATA.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record HorarioProyectoDTO(
        String dia,
        String horaInicio,
        String horaFin
) {}
