package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionDTO(
    Long id,
    UnidadDTO unidadRegional,
    UnidadDTO unidadArea,
    UnidadDTO unidadAreaPadre,
    String esAcademica,
    ConvocatoriaListadoDTO convocatoria
) {}
