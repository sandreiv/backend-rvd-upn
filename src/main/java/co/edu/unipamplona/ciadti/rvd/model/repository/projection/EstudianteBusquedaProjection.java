/**
 * Aplicación: rvd
 * Archivo: EstudianteBusquedaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface EstudianteBusquedaProjection {
    Long getIdPersonaGeneral();
    String getDocumento();
    String getNombreCompleto();
    Long getIdEstudiantePensum();
    Long getIdPrograma();
    String getNombrePrograma();
}
