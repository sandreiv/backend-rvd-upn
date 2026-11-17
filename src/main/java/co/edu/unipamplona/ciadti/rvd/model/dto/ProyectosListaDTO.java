/**
 * Aplicación: rvd
 * Archivo: ProyectosListaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 09/11/2026 - Leonel Antonio Perez Rios - Ajuste de atributos para listado de proyectos
 * 13/11/2026 - Integración de codigo, numeroActa, fechaActa y activo
 * 17/11/2026 - Inclusión de idProyectoGrupo y proyectoGrupoNombre
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record ProyectosListaDTO(
    Long id,
    String codigo,
    Long idTipoActividad,
    Long idCoordinacion,
    Long idProyectoPadre,
    Long idMateriaTdg,
    Long idProyectoGrupo,
    String proyectoGrupoNombre,
    String nombre,
    String descripcion,
    String actaConsejo,
    String numeroActa,
    Date fechaActa,
    String activo,
    IdNombreDTO tipoActividad,
    IdNombreDTO coordinacion,
    Integer totalDocentes
) {
    public ProyectosListaDTO(
        Long id,
        String codigo,
        Long idTipoActividad,
        Long idCoordinacion,
        Long idProyectoPadre,
        Long idMateriaTdg,
        String nombre,
        String descripcion,
        String actaConsejo,
        String numeroActa,
        Date fechaActa,
        String activo,
        IdNombreDTO tipoActividad,
        IdNombreDTO coordinacion,
        Integer totalDocentes
    ) {
        this(id, codigo, idTipoActividad, idCoordinacion, idProyectoPadre, idMateriaTdg,
             null, null, nombre, descripcion, actaConsejo, numeroActa, fechaActa,
             activo, tipoActividad, coordinacion, totalDocentes);
    }
}
