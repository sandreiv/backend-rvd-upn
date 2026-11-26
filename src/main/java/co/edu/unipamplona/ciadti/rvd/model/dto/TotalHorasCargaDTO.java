/**
 * Aplicación: rvd
 * Archivo: TotalHorasCargaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 26/11/2026
 * Modificaciones:
 * 26/11/2026 - Horas agrupadas por tipo de actividad de la coordinación.
 * 26/11/2026 - Árbol padre-hijo de tipos de actividad.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record TotalHorasCargaDTO(
    Long idTipoActividad,
    String nombre,
    BigDecimal horas,
    List<TotalHorasCargaDTO> hijos
) {}
