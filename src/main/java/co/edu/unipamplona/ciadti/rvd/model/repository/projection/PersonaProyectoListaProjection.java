/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoListaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 16/09/2026 - Andrés Hernández - Manejo relacion de actividad entre persona y proyecto
 * 09/11/2026 - Leonel Antonio Perez Rios - Adaptación de atributos para participantes de proyectos
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface PersonaProyectoListaProjection {
    Long getId();
    Long getIdProyecto();
    Long getIdPersonaGeneral();
    String getNombreCompleto();
    String getDocumento();
    default String getRol() { return null; }
    default String getTipo() { return null; }
    String getHoras();
    String getObservacion();
    String getEstado();
    String getEsActivo();
}
