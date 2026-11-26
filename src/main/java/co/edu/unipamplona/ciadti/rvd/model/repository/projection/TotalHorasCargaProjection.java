/**
 * Aplicación: rvd
 * Archivo: TotalHorasCargaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 26/11/2026
 * Modificaciones:
 * 26/11/2026 - Horas por tipo de actividad de la coordinación en una convocatoria.
 * 26/11/2026 - Padre y orden para armar el árbol de actividades.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;

public interface TotalHorasCargaProjection {

    Long getIdTipoActividad();

    Long getIdPadre();

    String getNombre();

    String getOrden();

    BigDecimal getHoras();
}
