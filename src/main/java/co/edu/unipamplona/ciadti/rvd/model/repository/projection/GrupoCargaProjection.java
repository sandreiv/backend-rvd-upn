/**
 * Aplicación: rvd
 * Archivo: GrupoCargaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface GrupoCargaProjection {

    Long getId();

    String getNombre();

    Long getCapacidad();

    Long getIdPeriodoUniversidad();

    Long getAnio();

    String getPeriodo();

    String getCodigoPeriodo();
}
