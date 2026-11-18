/**
 * Aplicación: rvd
 * Archivo: TipoActividadComponenteEnum.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.enums
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/11/2026
 * Modificaciones:
 * 10/11/2026 - Leonel Antonio Perez Rios - Creación inicial del enum de componentes de tipo de actividad
 * 17/11/2026 - Requisitos de campos de participantes (funcion, nivel, horario) por componente
 */
package co.edu.unipamplona.ciadti.rvd.model.enums;

import lombok.Getter;

@Getter
public enum TipoActividadComponenteEnum {
    DOCENCIADIRECTA("Docencia directa", false, false, false),
    ASIGNATURA("Asignatura", false, false, false),
    APOYO("Apoyo", false, false, false),
    INVESTIGACION("Investigación", false, false, false),
    DIRECCIONTDG("Dirección trabajo de grado", false, true, false),
    PROYECTOSINVESTIGACION("Proyectos de investigación", true, false, false),
    EXTENSION("Extensión", false, false, false),
    GESTIONINSTITUCIONAL("Gestión institucional", false, false, false),
    ACTIVIDADESPREDETERMINADAS("Actividades predeterminadas", false, false, false),
    PROYECTOSPLAN("Proyectos de plan", false, false, true),
    SITUACIONESADMINISTRATIVAS("Situaciones administrativas", false, false, false);

    private final String descripcion;
    private final boolean requiereFuncion;
    private final boolean requiereNivel;
    private final boolean requiereHorario;

    TipoActividadComponenteEnum(String descripcion, boolean requiereFuncion, boolean requiereNivel, boolean requiereHorario) {
        this.descripcion = descripcion;
        this.requiereFuncion = requiereFuncion;
        this.requiereNivel = requiereNivel;
        this.requiereHorario = requiereHorario;
    }

    public static TipoActividadComponenteEnum fromComponente(String componente) {
        if (componente == null || componente.isBlank()) {
            return null;
        }
        String normalized = componente.trim().toUpperCase();
        for (TipoActividadComponenteEnum val : values()) {
            if (val.name().equalsIgnoreCase(normalized)) {
                return val;
            }
        }
        return null;
    }
}