/**
 * Aplicación: rvd
 * Archivo: RestriccionProgramasEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/11/2026
 * Modificaciones:
 * 03/11/2026 - Creación según RVDIES.RESTRICCIONPROGRAMAS
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
@Table(name = "RESTRICCIONPROGRAMAS", schema = "RVDIES")
public class RestriccionProgramasEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPR_ID", nullable = false)
    private Long id;

    @Column(name = "PRCO_ID", nullable = false)
    private Long idProgramaCoordinacion;

    @Column(name = "PECO_ID", nullable = false)
    private Long idPersonaCoordinacion;

    @Column(name = "REPR_ESTADO")
    private String estado;

    @Column(name = "PRCO_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PRCO_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRCO_ID", insertable = false, updatable = false)
    private ProgramaCoordinacionEntity programaCoordinacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PECO_ID", insertable = false, updatable = false)
    private PersonaCoordinacionEntity personaCoordinacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "RestriccionProgramasEntity{"
                + "id=" + id
                + ", idProgramaCoordinacion=" + idProgramaCoordinacion
                + ", idPersonaCoordinacion=" + idPersonaCoordinacion
                + ", estado=" + estado
                + '}';
    }
}
