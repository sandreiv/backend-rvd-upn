package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record ValorPuntosCargaDTO(
    BigDecimal valorHora,
    BigDecimal valorPunto,
    BigDecimal puntosDocente,
    BigDecimal asignacionSalarial,
    String formaPago
) {}
