package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoVinculacionDTO(
        String estado,
        Long idClasificacionDocente,
        Long idDedicacionDocente,
        String minimo,
        String maximo,
        String formaPago,
        String tipoContrato,
        Integer esPlanta,
        String tipoHoras) {
}
