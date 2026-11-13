/**
 * Aplicación: rvd
 * Archivo: BloqueHoraDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/11/2026
 * Modificaciones:
 * 13/11/2026 - Bloque de hora del horario de clase.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record BloqueHoraDTO(
        Long id,
        String nombre,
        String horaInicio,
        String horaFinal
) {}
