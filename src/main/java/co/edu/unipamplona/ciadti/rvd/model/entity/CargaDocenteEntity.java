/**
 * Aplicación: rvd
 * Archivo: CargaDocenteEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 03/11/2026 - Alineación con RVDIES.CARGADOCENTE
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.ManyToOne;

@NamedStoredProcedureQuery(
    name = "CargaDocenteEntity.deleteByProcedure",
    procedureName = "RVDIES.PR_RVDIES_D_CARGADOCENTE",
    parameters = {
        @StoredProcedureParameter(name = "P_CADO_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_CADO_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "CARGADOCENTE", schema = "RVDIES")
public class CargaDocenteEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CADO_ID", nullable = false)
    private Long id;

    @Column(name = "PEGE_ID")
    private Long idPersonaGeneral;

    @Column(name = "TIVC_ID", nullable = false)
    private Long idTipoVinculacionConvocatoria;

    @Column(name = "CACA_ID", nullable = false)
    private Long idCategoriaCatedratico;

    @Column(name = "COOR_ID")
    private Long idCoordinacion;

    @Column(name = "ESTA_ID")
    private Long idEstado;

    @Column(name = "CADO_FECHAINICIO")
    private Date fechaInicio;

    @Column(name = "CADO_FECHAFIN")
    private Date fechaFin;

    @Column(name = "CADO_VALORCONTRATO")
    private BigDecimal valorContrato;

    @Column(name = "CADO_VALORPRESTACIONES")
    private BigDecimal valorPrestaciones;

    @Column(name = "CADO_SALARIO")
    private BigDecimal salario;

    @Column(name = "CADO_ESTADO")
    private String estado;

    @Column(name = "CADO_VIGENTE")
    private String vigente;

    @Column(name = "CADO_HORAS")
    private String horas;

    @Column(name = "CADO_VALORHORA")
    private BigDecimal valorHora;

    @Column(name = "CADO_PUNTOS")
    private String puntos;

    @Column(name = "CADO_VALORPUNTO")
    private BigDecimal valorPunto;

    @Column(name = "CADO_SEMANAS")
    private String semanas;

    @Column(name = "CADO_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "CADO_FECHACAMBIO")
    private Date fechaCambio;

    @Column(name = "CADO_TOTALCONTRATO")
    private BigDecimal totalContrato;

    @Column(name = "CADO_ONCEMESES")
    private String onceMeses;

    @Column(name = "CADO_ESNOVEDAD")
    private String esNovedad;

    @Column(name = "NOVE_ID")
    private Long idNovedad;

    @Column(name = "CADO_OBSERVACION")
    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIVC_ID", insertable = false, updatable = false)
    private TipoVinculacionConvocatoriaEntity tipoVinculacionConvocatoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COOR_ID", insertable = false, updatable = false)
    private CoordinacionesEntity coordinacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTA_ID", insertable = false, updatable = false)
    private EstadosEntity estadoCarga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOVE_ID", insertable = false, updatable = false)
    private NovedadEntity novedad;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CargaDocenteEntity{"
                + "id=" + id
                + ", idPersonaGeneral=" + idPersonaGeneral
                + ", idTipoVinculacionConvocatoria=" + idTipoVinculacionConvocatoria
                + ", idCategoriaCatedratico=" + idCategoriaCatedratico
                + ", idCoordinacion=" + idCoordinacion
                + ", idEstado=" + idEstado
                + ", estado=" + estado
                + ", vigente=" + vigente
                + ", esNovedad=" + esNovedad
                + ", idNovedad=" + idNovedad
                + '}';
    }
}
