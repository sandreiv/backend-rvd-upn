/**
 * Aplicación: rvd
 * Archivo: NovedadEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 03/11/2026 - Alineación con RVDIES.NOVEDADES
 * 24/11/2026 - Catálogo administrable sobre RVDIES.NOVEDADES
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Catálogo de novedades de carga docente (RVDIES.NOVEDADES).
 *
 * La tabla no tiene columna identity ni trigger, por eso la PK se toma de
 * SEQ_NOVEDADES con allocationSize = 1 (sin pooling de secuencias).
 */
@Getter
@Setter
@Entity
@Table(name = "NOVEDADES", schema = "RVDIES")
public class NovedadEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "novedades_seq"
    )
    @SequenceGenerator(
        name = "novedades_seq",
        sequenceName = "RVDIES.SEQ_NOVEDADES",
        allocationSize = 1
    )
    @Column(name = "NOVE_ID", nullable = false)
    private Long id;

    @Column(name = "NOVE_TIPO", nullable = false, length = 30)
    private String tipo;

    @Column(name = "NOVE_DESCRIPCION", nullable = false, length = 30)
    private String descripcion;

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
                + ", descripcion='" + descripcion + '\''
                + '}';
    }
}
