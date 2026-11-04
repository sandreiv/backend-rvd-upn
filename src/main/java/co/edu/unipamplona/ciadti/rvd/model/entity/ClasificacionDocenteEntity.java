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
@Table(name = "CLASIFICACIONDOCENTE", schema = "ACADEMICO")
public class ClasificacionDocenteEntity implements Serializable {
    @Id
    @Column(name = "CLDO_ID", nullable = false)
    private Long id;

    @Column(name = "CLDO_DESCRIPCION")
    private String descripcion;
}
