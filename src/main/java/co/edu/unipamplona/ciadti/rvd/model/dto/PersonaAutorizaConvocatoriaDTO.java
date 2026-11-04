package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PersonaAutorizaConvocatoriaDTO(
    Long id,
    String documentoIdentidad,
    String nombreCompleto,
    String telefonoCelular
) {}
