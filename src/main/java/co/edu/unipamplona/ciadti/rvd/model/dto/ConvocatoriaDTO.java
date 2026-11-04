package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record ConvocatoriaDTO(
    Long id,
    String nombre,
    String descripcion,
    Long idPeriodoUniversidad,
    String periodoUniversidad,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    String estado,
    Long idRelacion,
    String esIntersemestral
) {}
