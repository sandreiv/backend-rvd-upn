/**
 * Aplicación: rvd
 * Archivo: MateriaComponenteBancoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record MateriaComponenteBancoDTO(
        MateriaDTO materia,
        Long peso,
        String estado
) {}
