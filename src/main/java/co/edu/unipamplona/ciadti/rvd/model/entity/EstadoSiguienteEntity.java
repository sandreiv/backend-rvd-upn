/**
 * Aplicación: rvd
 * Archivo: EstadoSiguienteEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.ESTADOSIGUIENTE
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
@Table(name = "ESTADOSIGUIENTE", schema = "RVDIES")
public class EstadoSiguienteEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESSI_ID", nullable = false)
    private Long id;

    @Column(name = "ESTA_IDORIGEN", nullable = false)
    private Long idEstadoOrigen;

    @Column(name = "ESTA_IDDESTINO", nullable = false)
    private Long idEstadoDestino;

    @Column(name = "ESTA_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "ESTA_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTA_IDORIGEN", insertable = false, updatable = false)
    private EstadosEntity estadoOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTA_IDDESTINO", insertable = false, updatable = false)
    private EstadosEntity estadoDestino;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "EstadoSiguienteEntity{"
                + "id=" + id
                + ", idEstadoOrigen=" + idEstadoOrigen
                + ", idEstadoDestino=" + idEstadoDestino
                + '}';
    }
}
