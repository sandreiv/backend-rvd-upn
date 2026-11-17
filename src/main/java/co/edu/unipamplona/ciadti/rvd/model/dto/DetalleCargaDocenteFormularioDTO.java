/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record DetalleCargaDocenteFormularioDTO(
        Long idTipoActividad,
        BigDecimal horas,
        Long idCoordinacion,
        Long idUnidadRegional,
        Long idPrograma,
        DetalleMateriaFormularioDTO materia,
        DetalleMateriaFormularioDTO materiaBanco,
        Long idGrupo,
        String extras
) {}
