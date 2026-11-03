/**
 * Aplicación: rvd
 * Archivo: EstadoCargaEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 03/11/2026 - Alineación con RVDIES.ESTADOCARGA (historial de transición)
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
@Table(name = "ESTADOCARGA", schema = "RVDIES")
public class EstadoCargaEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESCA_ID", nullable = false)
    private Long id;

    @Column(name = "CADO_ID", nullable = false)
    private Long idCargaDocente;

    @Column(name = "ESTA_IDANTERIOR")
    private Long idEstadoAnterior;

    @Column(name = "ESTA_IDNUEVO")
    private Long idEstadoNuevo;

    @Column(name = "PEGE_ID", nullable = false)
    private Long idPersonaGeneral;

    @Column(name = "ESCA_COMENTARIO")
    private String comentario;

    @Column(name = "ESCA_FECHA")
    private Date fecha;

    @Column(name = "ESCA_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "ESCA_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CADO_ID", insertable = false, updatable = false)
    private CargaDocenteEntity cargaDocente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTA_IDANTERIOR", insertable = false, updatable = false)
    private EstadosEntity estadoAnterior;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTA_IDNUEVO", insertable = false, updatable = false)
    private EstadosEntity estadoNuevo;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "EstadoCargaEntity{"
                + "id=" + id
                + ", idCargaDocente=" + idCargaDocente
                + ", idEstadoAnterior=" + idEstadoAnterior
                + ", idEstadoNuevo=" + idEstadoNuevo
                + ", idPersonaGeneral=" + idPersonaGeneral
                + ", comentario=" + comentario
                + ", fecha=" + fecha
                + '}';
    }
}
