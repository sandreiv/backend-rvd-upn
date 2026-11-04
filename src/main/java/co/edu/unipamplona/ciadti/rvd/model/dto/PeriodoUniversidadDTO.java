/**
 * Aplicación: rvd
 * Archivo: PeriodoUniversidadDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/06/2026
 * Modificaciones:
 * 05/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PeriodoUniversidadDTO(
    Long id,
    @JsonProperty("anio")
    @JsonAlias({"ano", "PEUN_ANO"})
    Long anio,
    String periodo
) {
    @JsonProperty("ano")
    public Long ano() {
        return anio;
    }
}
/* 05/06/2026 @:Sebastian Jaimes */
