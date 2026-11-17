/**
 * Aplicación: rvd
 * Archivo: DiaSemanaEnum.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.enums
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - Enum para días estándar de la semana en horarios y dedicaciones
 */
package co.edu.unipamplona.ciadti.rvd.model.enums;

import lombok.Getter;

@Getter
public enum DiaSemanaEnum {
    LUNES("Lunes", "Lun"),
    MARTES("Martes", "Mar"),
    MIERCOLES("Miércoles", "Mié"),
    JUEVES("Jueves", "Jue"),
    VIERNES("Viernes", "Vie"),
    SABADO("Sábado", "Sáb"),
    DOMINGO("Domingo", "Dom");

    private final String nombre;
    private final String abreviacion;

    DiaSemanaEnum(String nombre, String abreviacion) {
        this.nombre = nombre;
        this.abreviacion = abreviacion;
    }
}
