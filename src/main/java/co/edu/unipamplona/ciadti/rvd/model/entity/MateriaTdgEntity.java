/**
 * Aplicación: rvd
 * Archivo: MateriaTdgEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.MATERIATDG
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
@Table(name = "MATERIATDG", schema = "RVDIES")
public class MateriaTdgEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MTDG_ID", nullable = false)
    private Long id;

    @Column(name = "MATE_CODIGOMATERIA")
    private Long codigoMateria;

    @Column(name = "PROG_ID")
    private Long idPrograma;

    @Column(name = "COTC_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "COTC_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "MateriaTdgEntity{"
                + "id=" + id
                + ", codigoMateria=" + codigoMateria
                + ", idPrograma=" + idPrograma
                + '}';
    }
}
