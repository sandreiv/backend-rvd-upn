/**
 * Aplicación: rvd
 * Archivo: ClaseSemanalEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/11/2026
 * Modificaciones:
 * 13/11/2026 - Creación según ACADEMICO.CLASESEMANAL.
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
@Table(name = "CLASESEMANAL", schema = "ACADEMICO")
public class ClaseSemanalEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLSE_ID")
    private Long id;

    @Column(name = "GRUP_ID")
    private Long idGrupo;

    @Column(name = "CLSE_DIA")
    private Long dia;

    @Column(name = "CLSE_FECHAINICIO")
    private Date fechaInicio;

    @Column(name = "CLSE_FECHAFINAL")
    private Date fechaFinal;

    @Column(name = "BLHO_IDINICIAL")
    private Long idBloqueHoraInicial;

    @Column(name = "BLHO_IDFINAL")
    private Long idBloqueHoraFinal;

    @Column(name = "CLSE_REGISTRADOPOR", length = 60)
    private String registradoPor;

    @Column(name = "CLSE_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ClaseSemanalEntity{"
                + "id=" + id
                + ", idGrupo=" + idGrupo
                + ", dia=" + dia
                + ", fechaInicio=" + fechaInicio
                + ", fechaFinal=" + fechaFinal
                + ", idBloqueHoraInicial=" + idBloqueHoraInicial
                + ", idBloqueHoraFinal=" + idBloqueHoraFinal
                + ", registradoPor=" + registradoPor
                + ", fechaCambio=" + fechaCambio
                + '}';
    }
}
