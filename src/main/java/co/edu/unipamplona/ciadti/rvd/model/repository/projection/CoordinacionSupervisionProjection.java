/**
 * Aplicación: rvd
 * Archivo: CoordinacionSupervisionProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/11/2026
 * Modificaciones:
 * 23/11/2026 - CIADTI - Proyección para listar árbol de coordinaciones a cargo y en supervisión
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface CoordinacionSupervisionProjection {

    Long getId();

    Long getIdPadre();

    String getNombre();

    String getEsAcademica();

    Integer getNivel();

    Integer getEsGestionable();
}
