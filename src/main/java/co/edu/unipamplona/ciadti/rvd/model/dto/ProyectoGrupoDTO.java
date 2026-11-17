/**
 * Aplicación: rvd
 * Archivo: ProyectoGrupoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ProyectoGrupoDTO(
    Long id,
    String nombre,
    String descripcion,
    String estado,
    List<Long> idConvocatorias,
    List<ConvocatoriaOpcionDTO> convocatorias
) {}
