package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoVinculacionesDTO(
        Long id,
        ClasificacionDocenteDTO clasificacionDocente,
        DedicacionDocenteDTO dedicacionDocente,
        String minimo,
        String maximo,
        String formaPago,
        String tipoContrato,
        Integer esPlanta,
        String tipoHoras
) {}
