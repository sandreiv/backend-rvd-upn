/**
 * Aplicación: rvd
 * Archivo: TipoDocumentoContratacionEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.TIPODOCUMENTOCONTRATACION
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
@Table(name = "TIPODOCUMENTOCONTRATACION", schema = "RVDIES")
public class TipoDocumentoContratacionEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIDC_ID", nullable = false)
    private Long id;

    @Column(name = "TIDC_NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "TIDC_DESCRIPCION", nullable = false)
    private String descripcion;

    @Column(name = "TIDC_OBLIGATORIO")
    private String obligatorio;

    @Column(name = "TIDC_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "TIDC_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TipoDocumentoContratacionEntity{"
                + "id=" + id
                + ", nombre=" + nombre
                + ", descripcion=" + descripcion
                + ", obligatorio=" + obligatorio
                + '}';
    }
}
