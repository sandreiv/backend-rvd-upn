/**
 * Aplicación: rvd
 * Archivo: EstudianteProyectoListaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record EstudianteProyectoListaDTO(
        Long id,
        Long idProyecto,
        Long idPersonaGeneral,
        Long idEstudiantePensum,
        String documento,
        String nombreCompleto,
        Long idPrograma,
        String nombrePrograma,
        Long idCentroCosto,
        String nombreCentroCosto
) {
}
