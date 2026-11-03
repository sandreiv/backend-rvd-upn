/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 * 03/11/2026 - Alineación con RVDIES.CONVOCATORIA
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
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.ParameterMode;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@NamedStoredProcedureQuery(
    name = "ConvocatoriaEntity.deleteByProcedure",
    procedureName = "RVDIES.PR_RVDIES_D_CONVOCATORIA",
    parameters = {
        @StoredProcedureParameter(name = "P_CONV_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_CONV_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "CONVOCATORIA", schema = "RVDIES")
public class ConvocatoriaEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONV_ID", nullable = false)
    private Long id;

    @Column(name = "PEUN_ID", nullable = false)
    private Long idPeriodoUniversidad;

    @Column(name = "CONV_IDRELACION")
    private Long idRelacion;

    @Column(name = "CONV_NOMBRE")
    private String nombre;

    @Column(name = "CONV_DESCRIPCION")
    private String descripcion;

    @Column(name = "CONV_ESTADO")
    private String estado;

    @Column(name = "CONV_FECHAINICIO")
    private Date fechaInicio;

    @Column(name = "CONV_FECHAFIN")
    private Date fechaFin;

    @Column(name = "CONV_ESINTERSEMESTRAL")
    private String esIntersemestral;

    @Column(name = "CONV_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "CONV_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEUN_ID", insertable = false, updatable = false)
    private PeriodoUniversidadEntity periodoUniversidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONV_IDRELACION", insertable = false, updatable = false)
    private ConvocatoriaEntity relacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ConvocatoriaEntity{"
                + "id=" + id
                + ", idPeriodoUniversidad=" + idPeriodoUniversidad
                + ", idRelacion=" + idRelacion
                + ", nombre=" + nombre
                + ", descripcion=" + descripcion
                + ", estado=" + estado
                + ", fechaInicio=" + fechaInicio
                + ", fechaFin=" + fechaFin
                + ", esIntersemestral=" + esIntersemestral
                + '}';
    }
}

/* 03/11/2026 @:Sebastian Jaimes */
