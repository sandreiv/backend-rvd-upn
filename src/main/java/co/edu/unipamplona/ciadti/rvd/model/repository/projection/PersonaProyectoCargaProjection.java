/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoCargaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - Proyectos de la persona de una carga docente.
 * 18/11/2026 - Columnas de RVDIES.TIPOACTIVIDADES en el tipo de actividad.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.sql.Clob;
import java.util.Date;

public interface PersonaProyectoCargaProjection {

    Long getId();

    Long getIdPersonaGeneral();

    Long getIdPrograma();

    Long getIdCentroCosto();

    String getHoras();

    String getObservacion();

    Clob getData();

    Long getIdProyecto();

    Long getIdProyectoPadre();

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

    Long getIdCoordinacion();

    Long getIdMateriaParticular();

    String getCodigoMateriaParticular();

    String getNombreMateriaParticular();

    Long getHorasPracticasMateriaParticular();

    Long getHorasTeoricasMateriaParticular();

    Long getHorasTeoricoPracticasMateriaParticular();

    Long getPonderacionMateriaParticular();

    Long getCapacidadMateriaParticular();

    Long getTieneGrupoMateriaParticular();

    Long getEsElectivaMateriaParticular();

    String getTipoMateriaParticular();

    Long getDocentesPorGrupoMateriaParticular();

    Long getIdProgramaMateriaParticular();

    String getNombreProgramaMateriaParticular();

    Long getIdCentroCostoProgramaMateriaParticular();

    String getDescripcionCentroCostoProgramaMateriaParticular();

    Long getIdProyectoGrupo();

    String getNombre();

    String getDescripcion();

    String getActaConsejo();

    String getNumeroActa();

    Date getFechaActa();

    String getCodigo();

    Long getActivo();
}
