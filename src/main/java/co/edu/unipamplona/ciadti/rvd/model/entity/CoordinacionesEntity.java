/**
 * Aplicación: rvd
 * Archivo: CoordinacionesEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 03/11/2026 - Alineación con RVDIES.COORDINACIONES
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@NamedStoredProcedureQuery(
    name = "CoordinacionesEntity.deleteByProcedure",
    procedureName = "RVDIES.PR_RVDIES_D_COORDINACIONES",
    parameters = {
        @StoredProcedureParameter(name = "P_COOR_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_COOR_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)

@Getter
@Setter
@Entity
@Table(name = "COORDINACIONES", schema = "RVDIES")
public class CoordinacionesEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COOR_ID", nullable = false)
    private Long id;

    @Column(name = "UNID_IDPADRE")
    private Long idUnidadPadre;

    @Column(name = "UNID_IDREGIONAL", nullable = false)
    private Long idUnidadRegional;

    @Column(name = "UNID_IDAREA", nullable = false)
    private Long idUnidadArea;

    @Column(name = "COOR_ESACADEMICA")
    private String esAcademica;

    @Column(name = "COOR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "COOR_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNID_IDREGIONAL", insertable = false, updatable = false)
    private UnidadEntity unidadRegional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNID_IDAREA", insertable = false, updatable = false)
    private UnidadEntity unidadArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNID_IDPADRE", insertable = false, updatable = false)
    private UnidadEntity unidadPadre;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CoordinacionesEntity{"
                + "id=" + id
                + ", idUnidadPadre=" + idUnidadPadre
                + ", idUnidadRegional=" + idUnidadRegional
                + ", idUnidadArea=" + idUnidadArea
                + ", esAcademica=" + esAcademica
                + '}';
    }
}
