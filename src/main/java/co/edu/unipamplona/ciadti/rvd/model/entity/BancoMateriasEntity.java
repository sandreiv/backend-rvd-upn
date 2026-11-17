/**
 * Aplicación: rvd
 * Archivo: BancoMateriasEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Creación según ACADEMICO.BANCODEMATERIAS.
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "BANCODEMATERIAS", schema = "ACADEMICO")
public class BancoMateriasEntity implements Serializable, Cloneable {

    @Id
    @Column(name = "BAMA_CODIGOMATERIA", nullable = false, length = 20)
    private String codigoMateria;

    @Column(name = "BAMA_TIPOPROMEDIONOTA", length = 15)
    private String tipoPromedioNota;

    @Column(name = "BAMA_CATIDADMATERIAS")
    private Long cantidadMaterias;

    @Column(name = "BAMA_CANTIDADCREDITOS")
    private Long cantidadCreditos;

    @Column(name = "BAMA_PERMITEEXCESO", length = 1)
    private String permiteExceso;

    @Column(name = "BAMA_TIPO", length = 15)
    private String tipo;

    @Column(name = "BAMA_PESO")
    private Long peso;

    @Column(name = "BAMA_ROTUNICA", length = 1)
    private String rotunica;

    @Column(name = "BAMA_REGISTRADOPOR", length = 60)
    private String registradoPor;

    @Column(name = "BAMA_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @Column(name = "BAMA_REPITEPROMEDIA", length = 1)
    private String repitePromedia;

    @Column(name = "BAMA_REPITENOPROMEDIA", length = 1)
    private String repiteNoPromedia;

    @Column(name = "BAMA_TIPOREQUISITO", length = 15)
    private String tipoRequisito;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "BancoMateriasEntity{"
                + "codigoMateria=" + codigoMateria
                + ", tipoPromedioNota=" + tipoPromedioNota
                + ", cantidadMaterias=" + cantidadMaterias
                + ", cantidadCreditos=" + cantidadCreditos
                + ", permiteExceso=" + permiteExceso
                + ", tipo=" + tipo
                + ", peso=" + peso
                + ", rotunica=" + rotunica
                + ", tipoRequisito=" + tipoRequisito
                + '}';
    }
}
