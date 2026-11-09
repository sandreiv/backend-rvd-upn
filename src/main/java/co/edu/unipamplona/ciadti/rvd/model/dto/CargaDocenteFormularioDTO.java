/**
 * Aplicación: rvd
 * Archivo: CargaDocenteFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/06/2026
 * Modificaciones:
 * 25/06/2026 - Sebastian Jaimes - Creación inicial
 * 27/08/2026 - Andrés Hernández - Manejo de fechas local
 * 06/11/2026 - Campos de RVDIES.CARGADOCENTE.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CargaDocenteFormularioDTO(
    Long idPersonaGeneral,
    Long idTipoVinculacionConvocatoria,
    Long idCategoriaCatedratico,
    Long idCoordinacion,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    BigDecimal valorContrato,
    BigDecimal valorPrestaciones,
    BigDecimal asignacionSalarial,
    BigDecimal totalContrato,
    String horas,
    BigDecimal valorHora,
    String puntos,
    BigDecimal valorPunto,
    String semanas,
    String esNovedad,
    Long idNovedad,
    String observacion
) {}
