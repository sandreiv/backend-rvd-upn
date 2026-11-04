package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DEDICACIONDOCENTE", schema = "ACADEMICO")
public class DedicacionDocenteEntity implements Serializable {
    @Id
    @Column(name = "DEDO_ID", nullable = false)
    private Long id;

    @Column(name = "DEDO_DESCRIPCION")
    private String descripcion;
}
