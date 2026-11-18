/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteCargaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Listado de detalle de carga desde RVDIES.
 * 18/11/2026 - Columnas de RVDIES.TIPOACTIVIDADES en el tipo de actividad.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.sql.Clob;

public interface DetalleCargaDocenteCargaProjection {

    Long getIdCargaDocente();

    Long getIdDetalleCargaDocente();

    String getHoras();

    String getExtras();

    Clob getData();

    Long getIdTipoActividad();

    Long getIdTipoActividadPadre();

    String getNombreTipoActividad();

    String getDescripcionTipoActividad();

    String getOrdenTipoActividad();

    String getEstadoTipoActividad();

    String getMinimoHorasTipoActividad();

    String getMaximoHorasTipoActividad();

    Long getIdDependenciaTipoActividad();

    String getDocenciaDirectaTipoActividad();

    String getComponenteTipoActividad();

    String getHorasPorSemestreTipoActividad();

    Long getAplicaProyectosTipoActividad();

    String getNombreTipoActividadPadre();

    String getDescripcionTipoActividadPadre();

    String getOrdenTipoActividadPadre();

    String getComponenteTipoActividadPadre();

    Long getIdUnidadRegional();

    String getNombreUnidadRegional();

    Long getIdPrograma();

    String getNombrePrograma();

    Long getIdPensum();

    String getDescripcionPensum();

    Long getAnioInicioPensum();

    String getPeriodoInicioPensum();

    Long getNumPeriodosPensum();

    Long getIdGrupo();

    String getNombreGrupo();

    Long getCapacidadGrupo();

    String getCodigoMateria();

    String getNombreMateria();

    Long getPonderacionAcademica();

    String getCodigoMateriaBanco();

    String getNombreMateriaBanco();

    Long getPonderacionAcademicaBanco();

    Long getIdCentroCosto();

    String getDescripcionCentroCosto();

    Long getIdPersonaProyecto();

    Long getIdProyecto();

    Long getIdProyectoPadre();

    String getNombreProyecto();

    String getDescripcionProyecto();

    String getNombreProyectoPadre();

    String getDescripcionProyectoPadre();
}
