/**
 * Aplicación: rvd
 * Archivo: FuncionProyectoEnum.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.enums
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - Creación inicial del enum de funciones de participantes en proyectos
 */
package co.edu.unipamplona.ciadti.rvd.model.enums;

import lombok.Getter;

@Getter
public enum FuncionProyectoEnum {
    INVESTIGADOR_PRINCIPAL("Investigador principal"),
    COINVESTIGADOR("Coinvestigador"),
    ASESOR("Asesor o Asistente de investigación");

    private final String descripcion;

    FuncionProyectoEnum(String descripcion) {
        this.descripcion = descripcion;
    }
}
