/**
 * Aplicación: rvd
 * Archivo: EstudianteProyectoListaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface EstudianteProyectoListaProjection {
    Long getId();
    Long getIdProyecto();
    Long getIdPersonaGeneral();
    Long getIdEstudiantePensum();
    String getDocumento();
    String getNombreCompleto();
    Long getIdPrograma();
    String getNombrePrograma();
    Long getIdCentroCosto();
    String getNombreCentroCosto();
}
