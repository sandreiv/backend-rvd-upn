package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoVinculacionListaProjection;

@Mapper(componentModel = "spring")
public interface TipoVinculacionMapper {

    default TipoVinculacionListadoDTO toListadoDto(List<TipoVinculacionListaProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return new TipoVinculacionListadoDTO(List.of());
        }
        return new TipoVinculacionListadoDTO(projections.stream().map(this::toDto).toList());
    }

    default TipoVinculacionDTO toDto(TipoVinculacionListaProjection projection) {
        return new TipoVinculacionDTO(
                projection.getId(),
                projection.getIdClasificacionDocente(),
                projection.getIdDedicacionDocente(),
                projection.getMinimo(),
                projection.getMaximo(),
                projection.getFormaPago(),
                projection.getTipoContrato(),
                projection.getEsPlanta(),
                projection.getTipoHoras());
    }
}
