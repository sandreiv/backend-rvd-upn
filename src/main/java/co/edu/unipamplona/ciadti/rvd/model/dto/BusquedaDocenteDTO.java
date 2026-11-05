package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record BusquedaDocenteDTO(
    Long id,
    String documentoIdentidad,
    String nombreCompleto,
    CategoriaCatedraticoDTO categoriaCatedratico,
    EscalafonDTO escalafon,
    CargaDocenteBusquedaDTO cargaDocente,
    BigDecimal valorPunto,
    BigDecimal asignacionSalarial
) {}
