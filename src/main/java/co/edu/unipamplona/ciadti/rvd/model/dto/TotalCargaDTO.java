/**
 * Aplicación: rvd
 * Archivo: TotalCargaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 26/11/2026
 * Modificaciones:
 * 26/11/2026 - Totales de docentes, dinero y horas de una coordinación en una convocatoria.
 * 26/11/2026 - Las horas de la carga se entregan en árbol padre-hijo.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record TotalCargaDTO(
    Long totalDocentes,
    BigDecimal totalPrestaciones,
    BigDecimal totalContratos,
    BigDecimal totalPreasignacion,
    List<TotalHorasCargaDTO> totalHorasCarga,
    BigDecimal totalHoras
) {}
