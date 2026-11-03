/**
 * Aplicación: rvd
 * Archivo: ProgramaCoordinacionEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.PROGRAMACOORDINACION
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

@Getter
@Setter
@Entity
@Table(name = "PROGRAMACOORDINACION", schema = "RVDIES")
public class ProgramaCoordinacionEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRCO_ID", nullable = false)
    private Long id;

    @Column(name = "COOR_ID", nullable = false)
    private Long idCoordinacion;

    @Column(name = "PROG_ID")
    private Long idPrograma;

    @Column(name = "PRCO_ESTADO")
    private String estado;

    @Column(name = "CECO_ID")
    private Long idCentroCosto;

    @Column(name = "PRCO_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PRCO_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COOR_ID", insertable = false, updatable = false)
    private CoordinacionesEntity coordinacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ProgramaCoordinacionEntity{"
                + "id=" + id
                + ", idCoordinacion=" + idCoordinacion
                + ", idPrograma=" + idPrograma
                + ", estado=" + estado
                + ", idCentroCosto=" + idCentroCosto
                + '}';
    }
}
