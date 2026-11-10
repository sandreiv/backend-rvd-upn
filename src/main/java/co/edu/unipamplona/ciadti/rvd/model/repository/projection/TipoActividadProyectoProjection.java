/**
 * Aplicación: rvd
 * Archivo: TipoActividadProyectoProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/11/2026
 * Modificaciones:
 * 10/11/2026 - Leonel Antonio Perez Rios - Proyección para actividades jerárquicas en proyectos
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface TipoActividadProyectoProjection {

    Long getId();

    Long getIdPadre();

    String getNombre();

    String getNombrePadre();

    String getDescripcion();

    String getComponente();

    Integer getEsSeleccionable();
}
