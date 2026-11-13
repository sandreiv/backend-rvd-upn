/**
 * Aplicación: rvd
 * Archivo: MateriaComponenteBancoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Creación según ACADEMICO.MATERIACOMPONENTEBANCO.
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(MateriaComponenteBancoEntityId.class)
@Table(name = "MATERIACOMPONENTEBANCO", schema = "ACADEMICO")
public class MateriaComponenteBancoEntity implements Serializable, Cloneable {

    @Id
    @Column(name = "MACB_BANCOMATERIA", nullable = false, length = 20)
    private String codigoBanco;

    @Id
    @Column(name = "MACB_SUBMATERIABANCO", nullable = false, length = 20)
    private String codigoSubmateria;

    @Column(name = "MACB_PESO")
    private Long peso;

    @Column(name = "MACB_REGISTRADOPOR", length = 30)
    private String registradoPor;

    @Column(name = "MACB_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @Column(name = "MACB_ACTIVA", length = 1)
    private String activa;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "MateriaComponenteBancoEntity{"
                + "codigoBanco=" + codigoBanco
                + ", codigoSubmateria=" + codigoSubmateria
                + ", peso=" + peso
                + ", activa=" + activa
                + '}';
    }
}
