/**
 * Aplicación: rvd
 * Archivo: HorarioClaseDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/11/2026
 * Modificaciones:
 * 13/11/2026 - Horario de clase de un grupo.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record HorarioClaseDTO(
        Long id,
        Long dia,
        Date fechaInicio,
        Date fechaFinal,
        BloqueHoraDTO bloqueInicial,
        BloqueHoraDTO bloqueFinal
) {}
