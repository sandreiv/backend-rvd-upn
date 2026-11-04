package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ClasificacionDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DedicacionDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionesDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoVinculacionListaProjection;

@Mapper(componentModel = "spring")
public interface TipoVinculacionMapper {

    default TipoVinculacionListadoDTO toListadoDto(List<TipoVinculacionListaProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return new TipoVinculacionListadoDTO(List.of());
        }
        return new TipoVinculacionListadoDTO(projections.stream().map(this::toDto).toList());
    }

    default TipoVinculacionesDTO toDto(TipoVinculacionListaProjection projection) {
        return new TipoVinculacionesDTO(
                projection.getId(),
                toClasificacionDocente(projection),
                toDedicacionDocente(projection),
                projection.getMinimo(),
                projection.getMaximo(),
                projection.getFormaPago(),
                projection.getTipoContrato(),
                projection.getEsPlanta(),
                projection.getTipoHoras());
    }

    private static ClasificacionDocenteDTO toClasificacionDocente(TipoVinculacionListaProjection projection) {
        if (projection.getIdClasificacionDocente() == null) {
            return null;
        }
        return new ClasificacionDocenteDTO(
                projection.getIdClasificacionDocente(),
                projection.getDescripcionClasificacionDocente());
    }

    private static DedicacionDocenteDTO toDedicacionDocente(TipoVinculacionListaProjection projection) {
        if (projection.getIdDedicacionDocente() == null) {
            return null;
        }
        return new DedicacionDocenteDTO(
                projection.getIdDedicacionDocente(),
                projection.getDescripcionDedicacionDocente());
    }
}
