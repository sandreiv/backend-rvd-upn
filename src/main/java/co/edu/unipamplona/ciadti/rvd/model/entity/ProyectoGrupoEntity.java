/**
 * Aplicación: rvd
 * Archivo: ProyectoGrupoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PROYECTOGRUPO", schema = "RVDIES")
public class ProyectoGrupoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRGR_ID", nullable = false)
    private Long id;

    @Column(name = "PRGR_NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "PRGR_DESCRIPCION")
    private String descripcion;

    @Column(name = "PRGR_ESTADO", nullable = false)
    private String estado;

    @Column(name = "PRGR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PRGR_FECHACAMBIO")
    private Date fechaCambio;

    @OneToMany(mappedBy = "proyectoGrupo", fetch = FetchType.LAZY)
    private List<GrupoConvocatoriaEntity> convocatorias;

    @OneToMany(mappedBy = "proyectoGrupo", fetch = FetchType.LAZY)
    private List<ProyectosEntity> proyectos;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
