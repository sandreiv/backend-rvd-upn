/**
 * Aplicación: rvd
 * Archivo: ProgramaCentrosCostoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/11/2026
 * Modificaciones:
 * 19/11/2026 - Programa con los centros de costo de PROGRAMACOORDINACION.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ProgramaCentrosCostoDTO(
        Long id,
        String nombre,
        List<CentroCostoDTO> centrosCosto
) {}
