/**
 * Aplicación: rvd
 * Archivo: ProgramaCoordinacionFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record ProgramaCoordinacionFormularioDTO(
        Long id,
        Long idCoordinacion,
        Long idPrograma,
        Long idCentroCosto,
        String estado
) {}