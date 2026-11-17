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
 * 13/11/2026 - Inclusión de centro de costo, programa, funcion y data
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
    Long idCentroCosto,
    String centroCostoNombre,
    String centroCostoCodigo,
    Long idPrograma,
    String funcion,
    String data,
    String estado,
    String esActivo
) {
    public PersonaProyectoListaDTO(
        Long id,
        Long idProyecto,
        Long idPersonaGeneral,
        String nombreCompleto,
        String documento,
        String horas,
        String observacion,
        String estado,
        String esActivo
    ) {
        this(id, idProyecto, idPersonaGeneral, nombreCompleto, documento, horas, observacion,
             null, null, null, null, null, null, estado, esActivo);
    }
}
