package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface CoordinacionPersonaProjection {

    Long getId();

    Long getIdUnidadRegional();

    String getNombreUnidadRegional();

    Long getIdUnidadArea();

    String getNombreUnidadArea();

    Long getIdUnidadAreaPadre();

    String getNombreUnidadAreaPadre();

    String getEsAcademica();

    Long getIdConvocatoria();

    String getNombreConvocatoria();

    String getDescripcionConvocatoria();

    String getEstadoConvocatoria();

    Long getIdPeriodoUniversidad();

    Long getAnioPeriodo();

    String getPeriodo();
}
