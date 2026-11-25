/**
 * Aplicación: rvd
 * Archivo: AsociacionCoordinacionCatalogosDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record AsociacionCoordinacionCatalogosDTO(
    List<CatalogoAdministracionDTO> coordinaciones,
    List<CatalogoAdministracionDTO> programas,
    List<CatalogoAdministracionDTO> centrosCosto,
    List<CatalogoAdministracionDTO> personas
) {}