package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record FechasConvocatoriaFormularioDTO(
    Long id,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    String semanas,
    String vacaciones,
    String onceMeses,
    String codigo
) {}
