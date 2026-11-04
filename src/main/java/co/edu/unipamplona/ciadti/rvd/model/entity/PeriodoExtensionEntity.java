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
@Table(name = "PERIODOEXTENSION", schema = "RVDIES")
public class PeriodoExtensionEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEEX_ID", nullable = false)
    private Long id;

    @Column(name = "CONV_ID", nullable = false)
    private Long idConvocatoria;

    @Column(name = "PEUN_ID", nullable = false)
    private Long idPeriodoUniversidad;

    @Column(name = "PEEX_REGISTRADOPOR", length = 200)
    private String registradoPor;

    @Column(name = "PEEX_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONV_ID", insertable = false, updatable = false)
    private ConvocatoriaEntity convocatoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEUN_ID", insertable = false, updatable = false)
    private PeriodoUniversidadEntity periodoUniversidad;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
