/**
 * Aplicación: rvd
 * Archivo: TipoActividadComponenteEnum.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.enums
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/11/2026
 * Modificaciones:
 * 10/11/2026 - Leonel Antonio Perez Rios - Creación inicial del enum de componentes de tipo de actividad
 * 17/11/2026 - Requisitos de campos de participantes (funcion, nivel, horario) por componente
 * 20/11/2026 - Inclusión de aplicaPrograma para definir si el componente asocia programa académico
 */
package co.edu.unipamplona.ciadti.rvd.model.enums;

import lombok.Getter;

@Getter
public enum TipoActividadComponenteEnum {
    DOCENCIADIRECTA("Docencia directa", false, false, false, false),
    ASIGNATURA("Asignatura", false, false, false, false),
    APOYO("Apoyo", false, false, false, false),
    INVESTIGACION("Investigación", false, false, false, false),
    DIRECCIONTDG("Dirección trabajo de grado", false, true, false, true),
    PROYECTOSINVESTIGACION("Proyectos de investigación", true, false, false, true),
    EXTENSION("Extensión", false, false, false, true),
    GESTIONINSTITUCIONAL("Gestión institucional", false, false, false, false),
    ACTIVIDADESPREDETERMINADAS("Actividades predeterminadas", false, false, false, false),
    PROYECTOSPLAN("Proyectos de plan", false, false, true, false),
    SITUACIONESADMINISTRATIVAS("Situaciones administrativas", false, false, false, false);

    private final String descripcion;
    private final boolean requiereFuncion;
    private final boolean requiereNivel;
    private final boolean requiereHorario;
    private final boolean aplicaPrograma;

    TipoActividadComponenteEnum(String descripcion, boolean requiereFuncion, boolean requiereNivel, boolean requiereHorario, boolean aplicaPrograma) {
        this.descripcion = descripcion;
        this.requiereFuncion = requiereFuncion;
        this.requiereNivel = requiereNivel;
        this.requiereHorario = requiereHorario;
        this.aplicaPrograma = aplicaPrograma;
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