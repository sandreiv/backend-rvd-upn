/**
 * Aplicación: rvd
 * Archivo: TipoVinculacionConvocatoriaEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.TIPOVINCULACIONCONVOCATORIA
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
@Table(name = "TIPOVINCULACIONCONVOCATORIA", schema = "RVDIES")
public class TipoVinculacionConvocatoriaEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIVC_ID", nullable = false)
    private Long id;

    @Column(name = "CONV_ID", nullable = false)
    private Long idConvocatoria;

    @Column(name = "TIVI_ID", nullable = false)
    private Long idTipoVinculacion;

    @Column(name = "TIVC_ESTADO")
    private String estado;

    @Column(name = "TIAM_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "TIAM_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONV_ID", insertable = false, updatable = false)
    private ConvocatoriaEntity convocatoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIVI_ID", insertable = false, updatable = false)
    private TipoVinculacionEntity tipoVinculacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TipoVinculacionConvocatoriaEntity{"
                + "id=" + id
                + ", idConvocatoria=" + idConvocatoria
                + ", idTipoVinculacion=" + idTipoVinculacion
                + ", estado=" + estado
                + '}';
    }
}
