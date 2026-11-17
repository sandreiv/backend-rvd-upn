/**
 * Aplicación: rvd
 * Archivo: MateriaPensumProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface MateriaPensumProjection {

    String getCodigoMateria();

    String getNombre();

    Long getHorasPracticas();

    Long getHorasTeoricas();

    Long getHorasTeoricoPracticas();

    Long getPeriodo();

    Long getPonderacionAcademica();

    Long getCapacidad();

    Long getTieneGrupo();

    Long getEsElectiva();

    Long getEsCompartida();
}
