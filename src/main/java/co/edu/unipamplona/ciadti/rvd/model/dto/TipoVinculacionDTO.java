package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoVinculacionDTO(
        Long id,
        Long idClasificacionDocente,
        Long idDedicacionDocente,
        String minimo,
        String maximo,
        String formaPago,
        String tipoContrato,
        Integer esPlanta,
        String tipoHoras
) {
    public TipoVinculacionDTO(
            Long id,
            Long idClasificacionDocente,
            Long idDedicacionDocente,
            String minimo,
            String maximo,
            String formaPago,
            String tipoContrato,
            String tipoHoras
    ) {
        this(id, idClasificacionDocente, idDedicacionDocente, minimo, maximo, formaPago, tipoContrato, null, tipoHoras);
    }
}
