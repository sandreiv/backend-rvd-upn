/**
 * Aplicación: rvd
 * Archivo: TipoActividadComponenteEnum.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.enums
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/11/2026
 * Modificaciones:
 * 10/11/2026 - Leonel Antonio Perez Rios - Creación inicial del enum de componentes de tipo de actividad
 */
package co.edu.unipamplona.ciadti.rvd.model.enums;

import lombok.Getter;

@Getter
public enum TipoActividadComponenteEnum {
    DOCENCIADIRECTA("Docencia directa"),
    ASIGNATURA("Asignatura"),
    APOYO("Apoyo"),
    INVESTIGACION("Investigación"),
    DIRECCIONTDG("Dirección trabajo de grado"),
    PROYECTOSINVESTIGACION("Proyectos de investigación"),
    EXTENSION("Extensión"),
    GESTIONINSTITUCIONAL("Gestión institucional"),
    ACTIVIDADESPREDETERMINADAS("Actividades predeterminadas"),
    PROYECTOSPLAN("Proyectos de plan"),
    SITUACIONESADMINISTRATIVAS("Situaciones administrativas");

    private final String descripcion;

    TipoActividadComponenteEnum(String descripcion) {
        this.descripcion = descripcion;
    }
}
