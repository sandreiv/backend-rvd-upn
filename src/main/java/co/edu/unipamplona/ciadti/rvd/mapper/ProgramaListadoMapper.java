/**
 * Aplicación: rvd
 * Archivo: ProgramaListadoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProgramaListadoProjection;

@Mapper(componentModel = "spring")
public interface ProgramaListadoMapper {

    default List<ProgramaDTO> toDtoList(List<ProgramaListadoProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default ProgramaDTO toDto(ProgramaListadoProjection projection) {
        if (projection == null) {
            return null;
        }
        CentroCostoDTO centroCosto = projection.getIdCentroCosto() == null
                ? null
                : new CentroCostoDTO(projection.getIdCentroCosto(), projection.getDescripcionCentroCosto());
        return new ProgramaDTO(projection.getId(), projection.getNombre(), centroCosto);
    }
}
