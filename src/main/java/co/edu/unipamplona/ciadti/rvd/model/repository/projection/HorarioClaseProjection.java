/**
 * Aplicación: rvd
 * Archivo: HorarioClaseProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/11/2026
 * Modificaciones:
 * 13/11/2026 - Horario de clase semanal con bloques de hora.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Date;

public interface HorarioClaseProjection {

    Long getId();

    Long getDia();

    Date getFechaInicio();

    Date getFechaFinal();

    Long getIdBloqueInicial();

    String getNombreBloqueInicial();

    String getHoraInicioBloqueInicial();

    String getHoraFinalBloqueInicial();

    Long getIdBloqueFinal();

    String getNombreBloqueFinal();

    String getHoraInicioBloqueFinal();

    String getHoraFinalBloqueFinal();
}
