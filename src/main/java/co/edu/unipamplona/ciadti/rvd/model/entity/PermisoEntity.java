/**
 * Aplicación: rvd
 * Archivo: PermisoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.PERMISOS
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
@Table(name = "PERMISOS", schema = "RVDIES")
public class PermisoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERM_ID", nullable = false)
    private Long id;

    @Column(name = "PERF_ID", nullable = false)
    private Long idPerfil;

    @Column(name = "ESSI_ID", nullable = false)
    private Long idEstadoSiguiente;

    @Column(name = "PERM_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PERM_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERF_ID", insertable = false, updatable = false)
    private PerfilEntity perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESSI_ID", insertable = false, updatable = false)
    private EstadoSiguienteEntity estadoSiguiente;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PermisoEntity{"
                + "id=" + id
                + ", idPerfil=" + idPerfil
                + ", idEstadoSiguiente=" + idEstadoSiguiente
                + '}';
    }
}
