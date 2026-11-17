/**
 * Aplicación: rvd
 * Archivo: MateriaProgramaCargaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - Materia de la carga con su programa y el centro de costo del programa.
 * 17/11/2026 - Grupo de la materia.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record MateriaProgramaCargaDTO(
        MateriaDetalleDTO materia,
        ProgramaDTO programa,
        GrupoDTO grupo
) {}
