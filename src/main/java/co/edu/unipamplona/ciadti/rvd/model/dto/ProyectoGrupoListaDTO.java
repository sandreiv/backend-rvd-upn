/**
 * Aplicación: rvd
 * Archivo: ProyectoGrupoListaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ProyectoGrupoListaDTO(
    Long id,
    String nombre,
    String descripcion,
    String estado,
    Integer totalProyectos,
    Integer totalConvocatorias,
    List<ConvocatoriaOpcionDTO> convocatorias
) {}
