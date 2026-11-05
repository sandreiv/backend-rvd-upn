package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CargaDocenteBusquedaDTO(
    Long id,
    Long idCarga,
    Long idConvocatoria,
    Long idCoordinacion,
    Long idTipoVinculacionConvocatoria
) {}

// SE DEBE EXTENDER DEPENDIENDO DE LO REQUERIDO
