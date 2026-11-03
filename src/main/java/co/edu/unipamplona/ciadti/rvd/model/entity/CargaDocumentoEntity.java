/**
 * Aplicación: rvd
 * Archivo: CargaDocumentoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.CARGADOCUMENTO
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
@Table(name = "CARGADOCUMENTO", schema = "RVDIES")
public class CargaDocumentoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDOC_ID", nullable = false)
    private Long id;

    @Column(name = "CADO_ID", nullable = false)
    private Long idCargaDocente;

    @Column(name = "CDOC_ADJUNTO")
    private String adjunto;

    @Column(name = "TIDC_ID", nullable = false)
    private Long idTipoDocumentoContratacion;

    @Column(name = "CDOC_ESTADO")
    private String estado;

    @Column(name = "PRCO_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PRCO_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CADO_ID", insertable = false, updatable = false)
    private CargaDocenteEntity cargaDocente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIDC_ID", insertable = false, updatable = false)
    private TipoDocumentoContratacionEntity tipoDocumentoContratacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CargaDocumentoEntity{"
                + "id=" + id
                + ", idCargaDocente=" + idCargaDocente
                + ", adjunto=" + adjunto
                + ", idTipoDocumentoContratacion=" + idTipoDocumentoContratacion
                + ", estado=" + estado
                + '}';
    }
}
