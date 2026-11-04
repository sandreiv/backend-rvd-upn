package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface TipoVinculacionListaProjection {

    Long getId();

    String getEstado();

    Long getIdClasificacionDocente();

    Long getIdDedicacionDocente();

    String getMinimo();

    String getMaximo();

    String getFormaPago();

    String getTipoContrato();

    Integer getEsPlanta();

    String getTipoHoras();
}
