/**
 * Aplicación: rvd
 * Archivo: DocenteCargaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Creación inicial
 * 09/11/2026 - Datos de RVDIES.ESTADOS en el listado.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;
import java.util.Date;

public interface DocenteCargaProjection {

    Long getIdPersonaGeneral();

    String getNombreCompleto();

    Long getIdCargaDocente();

    Long getIdTipoVinculacionConvocatoria();

    Long getIdCategoriaCatedratico();

    Long getIdCoordinacion();

    Long getIdEstado();

    String getNombreEstado();

    String getDescripcionEstado();

    String getOrdenEstado();

    String getTipoEstado();

    Date getCargaFechaInicio();

    Date getCargaFechaFin();

    BigDecimal getValorContrato();

    BigDecimal getValorPrestaciones();

    BigDecimal getAsignacionSalarial();

    BigDecimal getTotalContrato();

    String getHoras();

    BigDecimal getValorHora();

    String getPuntos();

    BigDecimal getValorPunto();

    String getSemanas();

    String getOnceMeses();

    Integer getTieneActividades();
}
