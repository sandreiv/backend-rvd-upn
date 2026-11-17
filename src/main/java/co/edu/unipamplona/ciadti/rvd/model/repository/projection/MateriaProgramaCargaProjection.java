/**
 * Aplicación: rvd
 * Archivo: MateriaProgramaCargaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - Materia, programa y centro de costo de una carga docente.
 * 17/11/2026 - Grupo de la materia.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface MateriaProgramaCargaProjection {

    String getCodigoMateria();

    String getNombreMateria();

    Long getPonderacionAcademica();

    Long getIdPrograma();

    String getNombrePrograma();

    Long getIdCentroCosto();

    String getDescripcionCentroCosto();

    Long getIdGrupo();

    String getNombreGrupo();

    Long getCapacidadGrupo();
}
