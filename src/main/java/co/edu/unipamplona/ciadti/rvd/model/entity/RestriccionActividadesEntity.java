/**
 * Aplicación: rvd
 * Archivo: RestriccionActividadesEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.RESTRICCIONACTIVIDADES
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
@Table(name = "RESTRICCIONACTIVIDADES", schema = "RVDIES")
public class RestriccionActividadesEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REAC_ID", nullable = false)
    private Long id;

    @Column(name = "TIVI_ID", nullable = false)
    private Long idTipoVinculacion;

    @Column(name = "TIAC_ID", nullable = false)
    private Long idTipoActividad;

    @Column(name = "TIDC_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "TIDC_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIVI_ID", insertable = false, updatable = false)
    private TipoVinculacionEntity tipoVinculacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIAC_ID", insertable = false, updatable = false)
    private TipoActividadesEntity tipoActividad;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "RestriccionActividadesEntity{"
                + "id=" + id
                + ", idTipoVinculacion=" + idTipoVinculacion
                + ", idTipoActividad=" + idTipoActividad
                + '}';
    }
}
