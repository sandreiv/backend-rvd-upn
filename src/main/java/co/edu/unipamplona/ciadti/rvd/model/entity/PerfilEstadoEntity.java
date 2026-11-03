/**
 * Aplicación: rvd
 * Archivo: PerfilEstadoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.PERFILESTADO
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
@Table(name = "PERFILESTADO", schema = "RVDIES")
public class PerfilEstadoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEES_ID", nullable = false)
    private Long id;

    @Column(name = "ESTA_ID", nullable = false)
    private Long idEstado;

    @Column(name = "PERF_ID", nullable = false)
    private Long idPerfil;

    @Column(name = "PEES_ESTADO")
    private String estado;

    @Column(name = "PEES_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PEES_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTA_ID", insertable = false, updatable = false)
    private EstadosEntity estadoCatalogo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERF_ID", insertable = false, updatable = false)
    private PerfilEntity perfil;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PerfilEstadoEntity{"
                + "id=" + id
                + ", idEstado=" + idEstado
                + ", idPerfil=" + idPerfil
                + ", estado=" + estado
                + '}';
    }
}
