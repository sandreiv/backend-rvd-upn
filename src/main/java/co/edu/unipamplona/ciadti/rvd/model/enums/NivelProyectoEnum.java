/**
 * Aplicación: rvd
 * Archivo: NivelProyectoEnum.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.enums
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.enums;

import lombok.Getter;

@Getter
public enum NivelProyectoEnum {
    PREGRADO("Pregrado"),
    ESPECIALIZACION("Especialización"),
    MAESTRIA("Maestría"),
    DOCTORADO("Doctorado"),
    POSDOCTORADO("Posdoctorado");

    private final String descripcion;

    NivelProyectoEnum(String descripcion) {
        this.descripcion = descripcion;
    }
}
