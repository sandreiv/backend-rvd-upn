package co.edu.unipamplona.ciadti.rvd.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PeriodoUniversidadDTO(
    Long id,
    @JsonProperty("anio")
    @JsonAlias({"ano", "PEUN_ANO"})
    Long anio,
    String periodo,
    String codigoPeriodo
) {
    public PeriodoUniversidadDTO(Long id, Long anio, String periodo) {
        this(id, anio, periodo, null);
    }

    @JsonProperty("ano")
    public Long ano() {
        return anio;
    }
}
