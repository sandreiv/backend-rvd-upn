/**
 * Aplicación: rvd
 * Archivo: EstudianteBusquedaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record EstudianteBusquedaDTO(
        Long idPersonaGeneral,
        String documento,
        String nombreCompleto,
        Long idEstudiantePensum,
        Long idPrograma,
        String nombrePrograma
) {
}
