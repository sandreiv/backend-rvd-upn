/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 03/11/2026 - Alineación con RVDIES.PERSONAPROYECTO
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
@Table(name = "PERSONAPROYECTO", schema = "RVDIES")
public class PersonaProyectoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEPR_ID", nullable = false)
    private Long id;

    @Column(name = "PEGE_ID", nullable = false)
    private Long idPersonaGeneral;

    @Column(name = "PROY_ID", nullable = false)
    private Long idProyecto;

    @Column(name = "PEPR_HORAS")
    private String horas;

    @Column(name = "PEPR_OBSERVACION")
    private String observacion;

    @Column(name = "PEPR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PEPR_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROY_ID", insertable = false, updatable = false)
    private ProyectosEntity proyecto;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PersonaProyectoEntity{"
                + "id=" + id
                + ", idPersonaGeneral=" + idPersonaGeneral
                + ", idProyecto=" + idProyecto
                + ", horas=" + horas
                + ", observacion=" + observacion
                + '}';
    }
}
