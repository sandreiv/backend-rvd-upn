package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoVinculacionListaProjection;

@Mapper(componentModel = "spring")
public interface TipoVinculacionMapper {

    default List<TipoVinculacionListaDTO> toListaDtoList(List<TipoVinculacionListaProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toListaDto).toList();
    }

    default TipoVinculacionListaDTO toListaDto(TipoVinculacionListaProjection projection) {
        return new TipoVinculacionListaDTO(
                projection.getId(),
                new TipoVinculacionDTO(
                        projection.getEstado(),
                        projection.getIdClasificacionDocente(),
                        projection.getIdDedicacionDocente(),
                        projection.getMinimo(),
                        projection.getMaximo(),
                        projection.getFormaPago(),
                        projection.getTipoContrato(),
                        projection.getEsPlanta(),
                        projection.getTipoHoras()));
    }
}
