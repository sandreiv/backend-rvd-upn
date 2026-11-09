/**
 * Aplicación: rvd
 * Archivo: DocenteCargaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Creación inicial
 * 09/11/2026 - Estado de la carga como objeto de RVDIES.ESTADOS.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DocenteCargaDTO(
        Long idPersonaGeneral,
        String nombreCompleto,
        Long idCargaDocente,
        Long idTipoVinculacionConvocatoria,
        Long idCategoriaCatedratico,
        Long idCoordinacion,
        EstadoCargaDTO estadoCarga,
        LocalDate cargaFechaInicio,
        LocalDate cargaFechaFin,
        BigDecimal valorContrato,
        BigDecimal valorPrestaciones,
        BigDecimal asignacionSalarial,
        BigDecimal totalContrato,
        String horas,
        BigDecimal valorHora,
        String puntos,
        BigDecimal valorPunto,
        String semanas,
        String onceMeses,
        Integer tieneActividades
) {}
