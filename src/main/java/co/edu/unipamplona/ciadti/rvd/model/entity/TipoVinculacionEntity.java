/**
 * Aplicación: rvd
 * Archivo: TipoVinculacionEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.TIPOVINCULACION
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Long idClaseDocente;

    @Column(name = "DEDO_ID", nullable = false)
    private Long idDedicacionDocente;

    @Column(name = "TIVI_MINIMO")
    private String minimo;

    @Column(name = "TIVI_MAXIMO")
    private String maximo;

    @Column(name = "TIVI_FORMAPAGO")
    private String formaPago;

    @Column(name = "TIVI_TIPOCONTRATO")
    private String tipoContrato;

    @Column(name = "TIVI_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "TIVI_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TipoVinculacionEntity{"
                + "id=" + id
                + ", idClaseDocente=" + idClaseDocente
                + ", idDedicacionDocente=" + idDedicacionDocente
                + ", minimo=" + minimo
                + ", maximo=" + maximo
                + ", formaPago=" + formaPago
                + ", tipoContrato=" + tipoContrato
                + '}';
    }
}
