/**
 * Aplicación: rvd
 * Archivo: ProyectosListaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 09/11/2026 - Leonel Antonio Perez Rios - Ajuste de atributos para listado de proyectos
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record ProyectosListaDTO(
    Long id,
    Long idTipoActividad,
    Long idCoordinacion,
    Long idProyectoPadre,
    Long idMateriaTdg,
    String nombre,
    String descripcion,
    String actaConsejo,
    String estado,
    IdNombreDTO tipoActividad,
    IdNombreDTO coordinacion,
    Integer totalDocentes
) {}
