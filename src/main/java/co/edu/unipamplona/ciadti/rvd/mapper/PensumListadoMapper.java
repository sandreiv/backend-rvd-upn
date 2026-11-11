/**
 * Aplicación: rvd
 * Archivo: PensumListadoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.PensumListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PensumListadoProjection;

@Mapper(componentModel = "spring")
public interface PensumListadoMapper {

    default List<PensumListadoDTO> toDtoList(List<PensumListadoProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default PensumListadoDTO toDto(PensumListadoProjection projection) {
        if (projection == null) {
            return null;
        }
        return new PensumListadoDTO(
                projection.getId(),
                projection.getDescripcion(),
                projection.getAnioInicio(),
                projection.getPeriodoInicio(),
                projection.getNumPeriodos());
    }
}
