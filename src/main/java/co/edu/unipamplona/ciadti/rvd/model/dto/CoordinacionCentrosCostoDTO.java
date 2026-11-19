/**
 * Aplicación: rvd
 * Archivo: CoordinacionCentrosCostoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/11/2026
 * Modificaciones:
 * 19/11/2026 - Centros de costo de la coordinación y programas agrupados.
 * 19/11/2026 - Nombre de la coordinación desde la unidad de área.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record CoordinacionCentrosCostoDTO(
        Long id,
        String nombre,
        List<CentroCostoDTO> centrosCosto,
        List<ProgramaCentrosCostoDTO> programas
) {}
