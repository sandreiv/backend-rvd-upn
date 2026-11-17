/**
 * Aplicación: rvd
 * Archivo: BloqueHorasEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/11/2026
 * Modificaciones:
 * 13/11/2026 - Creación según ACADEMICO.BLOQUEHORAS.
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
@Table(name = "BLOQUEHORAS", schema = "ACADEMICO")
public class BloqueHorasEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BLHO_ID")
    private Long id;

    @Column(name = "BLHO_NOMBRE", length = 20)
    private String nombre;

    @Column(name = "BLHO_HORAINICIO", length = 5)
    private String horaInicio;

    @Column(name = "BLHO_HORAFINAL", length = 5)
    private String horaFinal;

    @Column(name = "TIHO_ID")
    private Long idTipoHora;

    @Column(name = "BLHO_REGISTRADOPOR", length = 60)
    private String registradoPor;

    @Column(name = "BLHO_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "BloqueHorasEntity{"
                + "id=" + id
                + ", nombre=" + nombre
                + ", horaInicio=" + horaInicio
                + ", horaFinal=" + horaFinal
                + ", idTipoHora=" + idTipoHora
                + ", registradoPor=" + registradoPor
                + ", fechaCambio=" + fechaCambio
                + '}';
    }
}
