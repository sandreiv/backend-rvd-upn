/**
 * Aplicación: rvd
 * Archivo: MateriaComponenteBancoProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface MateriaComponenteBancoProjection {

    String getCodigoMateria();

    String getNombre();

    Long getHorasPracticas();

    Long getHorasTeoricas();

    Long getHorasTeoricoPracticas();

    Long getPonderacionAcademica();

    Long getCapacidad();

    Long getPeso();

    String getEstado();
}
