/**
 * Aplicación: rvd
 * Archivo: ProgramaCoordinacionListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record ProgramaCoordinacionListadoDTO(
        Long id,
        Long idCoordinacion,
        String coordinacion,
        Long idPrograma,
        String programa,
        Long idCentroCosto,
        String centroCosto,
        String estado
) {}