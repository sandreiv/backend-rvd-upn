/**
 * Aplicación: rvd
 * Archivo: ActividadHorasResumenDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/08/2026
 * Modificaciones:
 * 03/08/2026 - Creación inicial
 * 03/08/2026 - Detalle adicional para actividades FAD/directas
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record ActividadHorasResumenDTO(
    String tipo,
    String codigo,
    String nombre,
    BigDecimal totalHoras,
    List<ActividadDirectaDetalleDTO> detalles
) {}
