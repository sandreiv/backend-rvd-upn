/**
 * Aplicación: rvd
 * Archivo: ProyectosEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 03/11/2026 - Alineación con RVDIES.PROYECTOS
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
@Table(name = "PROYECTOS", schema = "RVDIES")
public class ProyectosEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROY_ID", nullable = false)
    private Long id;

    @Column(name = "PROY_IDPROYECTO")
    private Long idProyectoPadre;

    @Column(name = "COOR_ID", nullable = false)
    private Long idCoordinacion;

    @Column(name = "MAPA_ID")
    private Long idMateriaParticular;

    @Column(name = "PROY_NOMBRE")
    private String nombre;

    @Column(name = "PROY_DESCRIPCION")
    private String descripcion;

    @Column(name = "PROY_ACTACONSEJO")
    private String actaConsejo;

    @Column(name = "PROY_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PROY_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROY_IDPROYECTO", insertable = false, updatable = false)
    private ProyectosEntity proyectoPadre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COOR_ID", insertable = false, updatable = false)
    private CoordinacionesEntity coordinacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAPA_ID", insertable = false, updatable = false)
    private MateriaParticularEntity materiaParticular;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ProyectosEntity{"
                + "id=" + id
                + ", idProyectoPadre=" + idProyectoPadre
                + ", idCoordinacion=" + idCoordinacion
                + ", idMateriaParticular=" + idMateriaParticular
                + ", nombre=" + nombre
                + ", descripcion=" + descripcion
                + ", actaConsejo=" + actaConsejo
                + '}';
    }
}
