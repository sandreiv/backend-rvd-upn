package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoVinculacionListaDTO(
        Long id,
        String estado,
        TipoVinculacionDTO tipoVinculacion) {

    public TipoVinculacionListaDTO(Long id, TipoVinculacionDTO tipoVinculacion) {
        this(id, null, tipoVinculacion);
    }
}
