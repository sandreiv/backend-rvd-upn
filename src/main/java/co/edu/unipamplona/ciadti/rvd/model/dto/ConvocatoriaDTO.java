/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/06/2026
 * Modificaciones:
 * 03/06/2026 - Sebastian Jaimes - Creación inicial
 * 09/09/2026 - Sebastian Jaimes - Campo contratacion
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record ConvocatoriaDTO(
    Long id,
    Long idPeriodoUniversidad,
    Long idRelacion,
    String nombre,
    String descripcion,
    String estado,
    Date fechaInicio,
    Date fechaFin,
    String esIntersemestral,
    String periodoUniversidad,
    String nivelEducativo,
    String nombreCompleto,
    String contratacion
) {}
/* 03/06/2026 @:Sebastian Jaimes */