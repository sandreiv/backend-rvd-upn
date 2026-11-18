/**
 * Aplicación: rvd
 * Archivo: ProyectoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - Alineación con RVDIES.PROYECTOS.
 * 18/11/2026 - tipoActividad reemplaza el id suelto.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record ProyectoDTO(
        Long id,
        Long idProyectoPadre,
        TipoActividadDTO tipoActividad,
        Long idCoordinacion,
        Long idMateriaParticular,
        Long idProyectoGrupo,
        String nombre,
        String descripcion,
        String actaConsejo,
        String numeroActa,
        Date fechaActa,
        String codigo,
        Long activo
) {}
