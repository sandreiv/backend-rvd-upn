/**
 * Aplicación: rvd
 * Archivo: TipoActividadAdministracionListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 30/07/2026 - Se agrega campo componente.
 * 10/11/2026 - Leonel Antonio Perez Rios - Se agrega campo idPadre para visualización jerárquica
 * 10/11/2026 - Leonel Antonio Perez Rios - Depuración de campos inexistentes (codigo, ptd) y reordenamiento de orden
 * 10/11/2026 - Leonel Antonio Perez Rios - Adición de idDependencia, docenciaDirecta y horasPorSemestre
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadAdministracionListadoDTO(
        Long id,
        Long idPadre,
        Long idDependencia,
        String orden,
        String nombre,
        String descripcion,
        String componente,
        String minimoHoras,
        String maximoHoras,
        String docenciaDirecta,
        String horasPorSemestre,
        String estado
) {}

/* 17/07/2026 @:Daniel Arias */
