/**
 * Aplicación: rvd
 * Archivo: ProyectosListaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 09/11/2026 - Leonel Antonio Perez Rios - Adaptación de campos (TipoActividad, Coordinacion, ActaConsejo)
 * 13/11/2026 - Adición de codigo, numeroActa, fechaActa y activo
 * 17/11/2026 - Adición de idProyectoGrupo y nombreProyectoGrupo
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Date;

public interface ProyectosListaProjection {
    Long getId();
    String getCodigo();
    Long getIdTipoActividad();
    String getNombreTipoActividad();
    String getCodigoTipoActividad();
    Long getIdCoordinacion();
    String getNombreCoordinacion();
    Long getIdProyectoPadre();
    Long getIdMateriaTdg();
    Long getIdProyectoGrupo();
    String getNombreProyectoGrupo();
    String getNombre();
    String getDescripcion();
    String getActaConsejo();
    String getNumeroActa();
    Date getFechaActa();
    String getActivo();
    Integer getTotalDocentes();
    Integer getTotalEstudiantes();
}
