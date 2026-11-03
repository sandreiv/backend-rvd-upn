/**
 * Aplicación: rvd
 * Archivo: PersonaCoordinacionEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 03/11/2026 - Alineación con RVDIES.PERSONACOORDINACION
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
@Table(name = "PERSONACOORDINACION", schema = "RVDIES")
public class PersonaCoordinacionEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PECO_ID", nullable = false)
    private Long id;

    @Column(name = "COOR_ID", nullable = false)
    private Long idCoordinacion;

    @Column(name = "PEGE_ID", nullable = false)
    private Long idPersonaGeneral;

    @Column(name = "PECO_ESTADO")
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
        return "PersonaCoordinacionEntity{"
                + "id=" + id
                + ", idCoordinacion=" + idCoordinacion
                + ", idPersonaGeneral=" + idPersonaGeneral
                + ", estado=" + estado
                + '}';
    }
}
