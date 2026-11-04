package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;
import java.util.List;

public record ConvocatoriaFormularioDTO(
    Long id,
    Long idPeriodoUniversidad,
    Long idRelacion,
    String nombre,
    String descripcion,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    String estado,
    String esIntersemestral,
    List<TipoVinculacionConvocatoriaDTO> vinculaciones,
    List<PeriodoExtensionDTO> periodosExtension
) {
    public ConvocatoriaFormularioDTO(
        Long id,
        Long idPeriodoUniversidad,
        Long idRelacion,
        String nombre,
        String descripcion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String estado,
        String esIntersemestral,
        List<TipoVinculacionConvocatoriaDTO> vinculaciones
    ) {
        this(id, idPeriodoUniversidad, idRelacion, nombre, descripcion, fechaInicio, fechaFin, estado, esIntersemestral, vinculaciones, null);
    }
}
