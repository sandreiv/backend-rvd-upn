/**
 * Aplicación: rvd
 * Archivo: CentroCostoCoordinacionEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.CENTROCOSTOCOORDINACION
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
@Table(name = "CENTROCOSTOCOORDINACION", schema = "RVDIES")
public class CentroCostoCoordinacionEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CCCO_ID", nullable = false)
    private Long id;

    @Column(name = "CECO_ID", nullable = false)
    private Long idCentroCosto;

    @Column(name = "COOR_ID", nullable = false)
    private Long idCoordinacion;

    @Column(name = "CCCO_ESTADO")
    private String estado;

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
        return "CentroCostoCoordinacionEntity{"
                + "id=" + id
                + ", idCentroCosto=" + idCentroCosto
                + ", idCoordinacion=" + idCoordinacion
                + ", estado=" + estado
                + '}';
    }
}
