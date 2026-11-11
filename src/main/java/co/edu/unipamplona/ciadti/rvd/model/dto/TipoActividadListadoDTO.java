/**
 * Aplicación: rvd
 * Archivo: TipoActividadListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Creación inicial. Nodo del árbol de RVDIES.TIPOACTIVIDADES.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record TipoActividadListadoDTO(
        Long id,
        String nombre,
        String descripcion,
        String orden,
        String estado,
        String minimoHoras,
        String maximoHoras,
        Long idDependencia,
        String docenciaDirecta,
        String componente,
        String horasPorSemestre,
        List<TipoActividadListadoDTO> hijos
) {}

/* 09/11/2026 */
