/**
 * Aplicación: rvd
 * Archivo: NovedadEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 03/11/2026 - Alineación con RVDIES.NOVEDADES
 * 24/11/2026 - Juan Diego Florez Vera - Alineación a RVD.NOVEDADES (NOVE_ACCION,
 *              NOVE_COMPONENTE, NOVE_FORPLANTA) y eliminación por RVD.PR_RVD_D_NOVEDADES
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Catálogo de novedades (RVD.NOVEDADES).
 *
 * NOVE_COMPONENTE es el contrato con el frontend: identifica la novedad que
 * consume cada modal (por ejemplo add-professor / delete-professor).
 */
@NamedStoredProcedureQuery(
    name = "NovedadEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_NOVEDADES",
    parameters = {
        @StoredProcedureParameter(
            name = "P_NOVE_ID",
            mode = ParameterMode.IN,
            type = Long.class
        ),
        @StoredProcedureParameter(
            name = "P_NOVE_REGISTRADOPOR",
            mode = ParameterMode.IN,
            type = String.class
        ),
        @StoredProcedureParameter(
            name = "P_EXITO",
            mode = ParameterMode.OUT,
            type = BigDecimal.class
        )
    }
)
@Getter
@Setter
@Entity
@Table(name = "NOVEDADES", schema = "RVD")
public class NovedadEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOVE_ID", nullable = false)
    private Long id;

    @Column(name = "NOVE_TIPO", length = 150)
    private String tipo;

    @Column(name = "NOVE_DESCRIPCION", length = 250)
    private String descripcion;

    @Column(name = "NOVE_ACCION", length = 30)
    private String accion;

    @Column(name = "NOVE_COMPONENTE", length = 100)
    private String componente;

    @Column(name = "NOVE_FORPLANTA", length = 1)
    private String forPlanta;

    @Column(name = "NOVE_REGISTRADOPOR", length = 150)
    private String registradoPor;

    @Column(name = "NOVE_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "NovedadEntity{"
                + "id=" + id
                + ", tipo='" + tipo + '\''
                + ", accion='" + accion + '\''
                + ", componente='" + componente + '\''
                + ", forPlanta='" + forPlanta + '\''
                + '}';
    }
}
