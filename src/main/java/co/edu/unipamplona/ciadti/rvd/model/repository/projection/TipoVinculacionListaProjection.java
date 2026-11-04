package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface TipoVinculacionListaProjection {

    Long getId();

    Long getIdClasificacionDocente();

    String getDescripcionClasificacionDocente();

    Long getIdDedicacionDocente();

    String getDescripcionDedicacionDocente();

    String getMinimo();

    String getMaximo();

    String getFormaPago();

    String getTipoContrato();

    Integer getEsPlanta();

    String getTipoHoras();
}
