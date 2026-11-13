/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteActividadDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

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
    String extras
) {}

/* 17/07/2026 @:Daniel Arias */
