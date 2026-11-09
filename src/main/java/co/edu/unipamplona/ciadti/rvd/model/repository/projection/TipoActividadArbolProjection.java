/**
 * Aplicación: rvd
 * Archivo: TipoActividadArbolProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface TipoActividadArbolProjection {

    Long getId();

    Long getIdPadre();

    String getNombre();

    String getDescripcion();

    String getOrden();

    String getEstado();

    String getMinimoHoras();

    String getMaximoHoras();

    Long getIdDependencia();

    String getDocenciaDirecta();

    String getComponente();

    String getHorasPorSemestre();
}

/* 09/11/2026 */
