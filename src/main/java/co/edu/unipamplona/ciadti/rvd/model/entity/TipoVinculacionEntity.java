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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TIPOVINCULACION", schema = "RVDIES")
public class TipoVinculacionEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIVI_ID", nullable = false)
    private Long id;

    @Column(name = "CLDO_ID", nullable = false)
    private Long idClasificacionDocente;

    @Column(name = "DEDO_ID", nullable = false)
    private Long idDedicacionDocente;

    @Column(name = "TIVI_MINIMO", length = 30)
    private String minimo;

    @Column(name = "TIVI_MAXIMO", length = 30)
    private String maximo;

    @Column(name = "TIVI_FORMAPAGO", length = 30)
    private String formaPago;

    @Column(name = "TIVI_TIPOCONTRATO", length = 30)
    private String tipoContrato;

    @Column(name = "TIVI_ESPLANTA")
    private Integer esPlanta;

    @Column(name = "TIVI_TIPOHORAS")
    private String tipoHoras;

    @Column(name = "TIVI_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "TIVI_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLDO_ID", insertable = false, updatable = false)
    private ClasificacionDocenteEntity clasificacionDocente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEDO_ID", insertable = false, updatable = false)
    private DedicacionDocenteEntity dedicacionDocente;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
