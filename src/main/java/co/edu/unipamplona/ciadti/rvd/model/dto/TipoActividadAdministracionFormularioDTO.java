/**
 * Aplicación: rvd
 * Archivo: TipoActividadAdministracionFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 30/07/2026 - Se agrega campo componente.
 * 10/11/2026 - Leonel Antonio Perez Rios - Depuración de campos inexistentes en esquema (codigo, ptd)
 * 10/11/2026 - Leonel Antonio Perez Rios - Adición de campo orden configurable
 * 10/11/2026 - Leonel Antonio Perez Rios - Adición de idDependencia, docenciaDirecta y horasPorSemestre
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadAdministracionFormularioDTO(
        String nombre,
        String descripcion,
        String componente,
        String orden,
        Long idDependencia,
        String docenciaDirecta,
        String horasPorSemestre,
        Integer minimoHoras,
        Integer maximoHoras,
        String estado
) {}

/* 17/07/2026 @:Daniel Arias */
