/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 03/11/2026 - Alineación con RVDIES.DETALLECARGADOCENTE
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
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import java.math.BigDecimal;

@NamedStoredProcedureQuery(
    name = "DetalleCargaDocenteEntity.deleteByProcedure",
    procedureName = "RVDIES.PR_RVDIES_D_DETALLECARGADOCENTE",
    parameters = {
        @StoredProcedureParameter(name = "P_DECD_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_DECD_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "DETALLECARGADOCENTE", schema = "RVDIES")
public class DetalleCargaDocenteEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DECD_ID", nullable = false)
    private Long id;

    @Column(name = "CADO_ID", nullable = false)
    private Long idCargaDocente;

    @Column(name = "PROG_ID")
    private Long idPrograma;

    @Column(name = "GRUP_ID")
    private Long idGrupo;

    @Column(name = "TIAC_ID", nullable = false)
    private Long idTipoActividad;

    @Column(name = "CECO_ID")
    private Long idCentroCosto;

    @Column(name = "PEPR_ID")
    private Long idPersonaProyecto;

    @Column(name = "MATE_CODIGOMATERIA")
    private Long codigoMateria;

    @Column(name = "COOR_ID")
    private Long idCoordinacion;

    @Column(name = "DECD_HORAS")
    private String horas;

    @Column(name = "DECD_DESCRIPCION")
    private String descripcion;

    @Column(name = "DECD_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "DECD_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CADO_ID", insertable = false, updatable = false)
    private CargaDocenteEntity cargaDocente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEPR_ID", insertable = false, updatable = false)
    private PersonaProyectoEntity personaProyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COOR_ID", insertable = false, updatable = false)
    private CoordinacionesEntity coordinacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "DetalleCargaDocenteEntity{"
                + "id=" + id
                + ", idCargaDocente=" + idCargaDocente
                + ", idPrograma=" + idPrograma
                + ", idGrupo=" + idGrupo
                + ", idTipoActividad=" + idTipoActividad
                + ", idCentroCosto=" + idCentroCosto
                + ", idPersonaProyecto=" + idPersonaProyecto
                + ", codigoMateria=" + codigoMateria
                + ", idCoordinacion=" + idCoordinacion
                + ", horas=" + horas
                + ", descripcion=" + descripcion
                + '}';
    }
}
