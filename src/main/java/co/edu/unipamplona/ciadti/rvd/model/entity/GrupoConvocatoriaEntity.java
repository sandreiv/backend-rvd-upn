/**
 * Aplicación: rvd
 * Archivo: GrupoConvocatoriaEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
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
@Table(name = "GRUPOCONVOCATORIA", schema = "RVDIES")
public class GrupoConvocatoriaEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GRCO_ID", nullable = false)
    private Long id;

    @Column(name = "PRGR_ID", nullable = false)
    private Long idProyectoGrupo;

    @Column(name = "CONV_ID", nullable = false)
    private Long idConvocatoria;

    @Column(name = "GRCO_ESTADO", nullable = false)
    private String estado;

    @Column(name = "GRCO_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "GRCO_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRGR_ID", insertable = false, updatable = false)
    private ProyectoGrupoEntity proyectoGrupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONV_ID", insertable = false, updatable = false)
    private ConvocatoriaEntity convocatoria;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
