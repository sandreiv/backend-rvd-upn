/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoListaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 16/09/2026 - Andrés Hernández - Manejo del estado de actividad entre docente y proyecto
 * 09/11/2026 - Leonel Antonio Perez Rios - Ajuste de atributos para listado de participantes
 * 10/11/2026 - Leonel Antonio Perez Rios - Remoción del campo inexistente rol
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PersonaProyectoListaDTO(
    Long id,
    Long idProyecto,
    Long idPersonaGeneral,
    String nombreCompleto,
    String documento,
    String horas,
    String observacion,
    String estado,
    String esActivo
) {}
