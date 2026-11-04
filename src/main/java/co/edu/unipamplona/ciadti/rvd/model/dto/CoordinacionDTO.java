package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionDTO(
    Long id,
    Long idCoordinacionPadre,
    UnidadDTO unidadRegional,
    UnidadDTO unidadArea,
    UnidadDTO unidadAreaPadre,
    String esAcademica,
    ConvocatoriaListadoDTO convocatoria
) {}
