package co.edu.unipamplona.ciadti.rvd.model.enums;

import lombok.Getter;

@Getter
public enum TipoHorasEnum {
    SEMANAL("Semanal"),
    SEMESTRAL("Semestral");

    private final String descripcion;

    TipoHorasEnum(String descripcion) {
        this.descripcion = descripcion;
    }
}
