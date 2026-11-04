package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface TipoVinculacionListaProjection {

    Long getId();

    Long getIdClasificacionDocente();

    Long getIdDedicacionDocente();

    String getMinimo();

    String getMaximo();

    String getFormaPago();

    String getTipoContrato();

    Integer getEsPlanta();

    String getTipoHoras();
}
