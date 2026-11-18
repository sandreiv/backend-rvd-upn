/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - Alineación con RVDIES.PERSONAPROYECTO.
 * 17/11/2026 - funcion, nivel y horario salen de PEPR_DATA.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record PersonaProyectoDTO(
        Long id,
        Long idPersonaGeneral,
        Long idPrograma,
        Long idCentroCosto,
        String horas,
        String observacion,
        ProyectoDTO proyecto,
        String funcion,
        String nivel,
        List<HorarioProyectoDTO> horario
) {}
