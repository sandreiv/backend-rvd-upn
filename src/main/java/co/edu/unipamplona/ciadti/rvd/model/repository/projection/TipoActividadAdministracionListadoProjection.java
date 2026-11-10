package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface TipoActividadAdministracionListadoProjection {
    Long getId();
    Long getIdPadre();
    Long getIdDependencia();
    String getOrden();
    String getNombre();
    String getDescripcion();
    String getComponente();
    String getMinimoHoras();
    String getMaximoHoras();
    String getDocenciaDirecta();
    String getHorasPorSemestre();
    String getEstado();
}
