/**
 * Aplicación: rvd
 * Archivo: TipoActividadDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 18/11/2026 - Alineación con RVDIES.TIPOACTIVIDADES.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadDTO(
        Long id,
        Long idPadre,
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
        Long aplicaProyectos
) {}
