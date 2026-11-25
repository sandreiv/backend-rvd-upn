package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record EstadoSiguienteFormularioDTO(
        List<Long> estadosDestinoIds
) {
}