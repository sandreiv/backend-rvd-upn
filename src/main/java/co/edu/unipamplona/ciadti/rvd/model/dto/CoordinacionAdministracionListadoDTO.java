/**
 * Aplicación: rvd
 * Archivo: CoordinacionAdministracionListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionAdministracionListadoDTO(
        Long id,
        Long idCoordinacionPadre,

        Long idUnidadRegional,
        String unidadRegional,

        Long idUnidad,
        String unidad,
        
        String esAcademica
) {}