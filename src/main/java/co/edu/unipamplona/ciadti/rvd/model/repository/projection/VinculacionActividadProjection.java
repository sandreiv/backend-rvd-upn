/**
 * Aplicación: rvd
 * Archivo: VinculacionActividadProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Juan Diego Florez Vera - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface VinculacionActividadProjection {
    Long getId();
    Long getIdTipoVinculacion();
    String getTipoVinculacion();
    Long getIdTipoActividad();
    String getTipoActividad();
    String getEstado();
    String getMinimoHoras();
    String getMaximoHoras();
}
