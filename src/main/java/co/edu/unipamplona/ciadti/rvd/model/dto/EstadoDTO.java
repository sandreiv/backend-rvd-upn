package co.edu.unipamplona.ciadti.rvd.model.dto;

public record EstadoDTO(
        Long id,
        String nombre,
        String descripcion,
        String tipo,
        String orden
) {
}