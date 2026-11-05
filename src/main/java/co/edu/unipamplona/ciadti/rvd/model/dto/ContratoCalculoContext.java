/**
 * Aplicación: rvd
 * Archivo: ContratoCalculoContext.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoCalculoContext(
        BigDecimal horasSemanales,
        BigDecimal semanas,
        BigDecimal valorHora,
        BigDecimal salario,
        BigDecimal valorPunto,
        String puntos,
        LocalDate fechaInicio,
        LocalDate fechaFin
) {}
