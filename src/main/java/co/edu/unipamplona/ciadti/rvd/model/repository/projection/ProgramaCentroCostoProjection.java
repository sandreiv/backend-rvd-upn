/**
 * Aplicación: rvd
 * Archivo: ProgramaCentroCostoProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/11/2026
 * Modificaciones:
 * 19/11/2026 - Programa y centro de costo de PROGRAMACOORDINACION.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface ProgramaCentroCostoProjection {

    Long getIdPrograma();

    String getNombrePrograma();

    Long getIdCentroCosto();

    String getDescripcionCentroCosto();
}
