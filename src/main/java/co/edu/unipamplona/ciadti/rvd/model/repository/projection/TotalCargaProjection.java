/**
 * Aplicación: rvd
 * Archivo: TotalCargaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 26/11/2026
 * Modificaciones:
 * 26/11/2026 - Conteo y montos de la carga de una coordinación en una convocatoria.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;

public interface TotalCargaProjection {

    BigDecimal getTotalDocentes();

    BigDecimal getTotalPrestaciones();

    BigDecimal getTotalContratos();

    BigDecimal getTotalPreasignacion();
}
