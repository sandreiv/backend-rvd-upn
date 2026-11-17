/**
 * Aplicación: rvd
 * Archivo: TitularGrupoProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - Carga dueña del detalle más antiguo de un grupo.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface TitularGrupoProjection {

    Long getIdGrupo();

    Long getIdCargaDocente();
}
