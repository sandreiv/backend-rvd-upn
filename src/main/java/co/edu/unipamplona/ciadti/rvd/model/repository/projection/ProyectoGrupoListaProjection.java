/**
 * Aplicación: rvd
 * Archivo: ProyectoGrupoListaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface ProyectoGrupoListaProjection {
    Long getId();
    String getNombre();
    String getDescripcion();
    String getEstado();
    Integer getTotalProyectos();
    Integer getTotalConvocatorias();
}
