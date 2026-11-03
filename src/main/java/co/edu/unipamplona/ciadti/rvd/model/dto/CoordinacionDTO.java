package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionDTO(
    Long id,
    Long idUnidadPadre,
    UnidadDTO unidadRegional,
    UnidadDTO unidadArea,
    String esAcademica,
    ConvocatoriaListadoDTO convocatoria
) {}
