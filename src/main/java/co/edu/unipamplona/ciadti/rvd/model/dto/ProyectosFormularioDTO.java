/**
 * Aplicación: rvd
 * Archivo: ProyectosFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 27/08/2026 - Andrés Hernández - Manejo de fechas local
 * 09/11/2026 - Leonel Antonio Perez Rios - Ajuste de atributos para gestión de proyectos
 * 13/11/2026 - Integración de codigo, numeroActa, fechaActa y activo
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record ProyectosFormularioDTO(
    String codigo,
    String nombre,
    String descripcion,
    String numeroActa,
    Date fechaActa,
    String actaConsejo,
    Long idTipoActividad,
    Long idCoordinacion,
    Long idProyectoPadre,
    Long idMateriaParticular,
    String activo
) {}
