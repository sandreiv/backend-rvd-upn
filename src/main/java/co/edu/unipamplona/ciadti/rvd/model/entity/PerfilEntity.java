/**
 * Aplicación: rvd
 * Archivo: PerfilEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.PERFILES
 * 27/11/2026 - Incorporación de código e indicador de proceso
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
@Table(name = "PERFILES", schema = "RVDIES")
public class PerfilEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERF_ID", nullable = false)
    private Long id;

    @Column(name = "PERF_NOMBRE", length = 150)
    private String nombre;

    @Column(name = "PERF_DESCRIPCION", length = 150)
    private String descripcion;

    @Column(name = "PERF_PROCESO", length = 500)
    private String proceso;

    @Column(name = "PERF_ESPROCESO", length = 1)
    private String esProceso;

    @Column(name = "PERF_CODIGO", length = 30)
    private String codigo;

    @Column(name = "PERF_REGISTRADOPOR", length = 150)
    private String registradoPor;

    @Column(name = "PERF_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {

        return "PerfilEntity{"
                + "id=" + id
                + ", nombre=" + nombre
                + ", descripcion=" + descripcion
                + ", proceso=" + proceso
                + ", esProceso=" + esProceso
                + ", codigo=" + codigo
                + '}';
    }
}