/**
 * Aplicación: rvd
 * Archivo: PensumListadoProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface PensumListadoProjection {

    Long getId();

    String getDescripcion();

    Long getAnioInicio();

    String getPeriodoInicio();

    Long getNumPeriodos();
}
