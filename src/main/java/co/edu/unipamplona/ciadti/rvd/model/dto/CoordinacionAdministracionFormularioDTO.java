/**
 * Aplicación: rvd
 * Archivo: CoordinacionAdministracionFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionAdministracionFormularioDTO(
        Long idUnidadRegional,
        Long idUnidad,
        String esAcademica
) {}