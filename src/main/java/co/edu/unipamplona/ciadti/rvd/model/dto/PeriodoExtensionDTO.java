package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PeriodoExtensionDTO(
    Long id,
    Long idPeriodoUniversidad,
    String nombrePeriodo
) {
    public PeriodoExtensionDTO(Long idPeriodoUniversidad) {
        this(null, idPeriodoUniversidad, null);
    }
}
