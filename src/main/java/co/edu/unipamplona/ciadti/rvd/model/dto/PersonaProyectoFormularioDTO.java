/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 16/09/2026 - Andrés Hernández - Manejo del estado de actividad entre docente y proyecto
 * 09/11/2026 - Leonel Antonio Perez Rios - Ajuste de atributos para participantes de proyectos
 * 10/11/2026 - Leonel Antonio Perez Rios - Inclusión de idTipoActividad y remoción de rol inexistente en BD
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PersonaProyectoFormularioDTO(
    Long idProyecto,
    Long idPersonaGeneral,
    Long idTipoActividad,
    String horas,
    String observacion,
    String estado,
    String esActivo
) {
    public PersonaProyectoFormularioDTO(
        Long idProyecto,
        Long idPersonaGeneral,
        String horas,
        String observacion,
        String estado,
        String esActivo
    ) {
        this(idProyecto, idPersonaGeneral, null, horas, observacion, estado, esActivo);
    }
}
