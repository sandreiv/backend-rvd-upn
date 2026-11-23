/**
 * Aplicación: rvd
 * Archivo: CoordinacionSupervisionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/11/2026
 * Modificaciones:
 * 23/11/2026 - CIADTI - DTO para árbol de coordinaciones a cargo y en supervisión
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionSupervisionDTO(
        Long id,
        Long idPadre,
        String nombre,
        String esAcademica,
        Integer nivel,
        Boolean esGestionable
) {}
