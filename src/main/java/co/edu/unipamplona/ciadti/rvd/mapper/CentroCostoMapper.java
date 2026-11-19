/**
 * Aplicación: rvd
 * Archivo: CentroCostoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/11/2026
 * Modificaciones:
 * 19/11/2026 - Listado de centros de costo.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;

@Mapper(componentModel = "spring")
public interface CentroCostoMapper {

    default List<CentroCostoDTO> toDtoList(List<CatalogoAdministracionProjection> centrosCosto) {
        if (centrosCosto == null || centrosCosto.isEmpty()) {
            return List.of();
        }
        return centrosCosto.stream()
                .map(centro -> new CentroCostoDTO(centro.getId(), centro.getLabel()))
                .toList();
    }
}
