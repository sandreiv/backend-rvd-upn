/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteActividadDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 13/11/2026 - data expone DECD_DATA.
 * 17/11/2026 - tipoDocente expone el indicativo TITULAR guardado en DECD_DATA.
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

import co.edu.unipamplona.ciadti.rvd.model.dto.HorarioClaseDTO;
import java.util.List;

public record DetalleCargaDocenteActividadDTO(
    TipoActividadDTO tipoActividad,
    UnidadDTO unidadRegional,
    ProgramaDTO programa,
    MateriaDetalleDTO materia,
    GrupoDTO grupo,
    CentroCostoDTO centroCosto,
    String horas,
    PensumDTO pensum,
    MateriaDetalleDTO materiaBanco,
    String extras,
    List<HorarioClaseDTO> clasesSemanales,
    String tipoDocente
) {}

/* 17/07/2026 @:Daniel Arias */
