package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionBusquedaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CoordinacionesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionPersonaProjection;

@Mapper(componentModel = "spring")
public interface CoordinacionMapper {

    default List<CoordinacionDTO> toDtoList(List<CoordinacionPersonaProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default CoordinacionDTO toDto(CoordinacionPersonaProjection projection) {
        return new CoordinacionDTO(
                projection.getId(),
                toUnidad(projection.getIdUnidadRegional(), projection.getNombreUnidadRegional()),
                toUnidad(projection.getIdUnidadArea(), projection.getNombreUnidadArea()),
                toUnidad(projection.getIdUnidadAreaPadre(), projection.getNombreUnidadAreaPadre()),
                projection.getEsAcademica(),
                toConvocatoria(projection));
    }

    default UnidadDTO toUnidad(Long id, String nombre) {
        if (id == null) {
            return null;
        }
        return new UnidadDTO(id, nombre);
    }

    default ConvocatoriaListadoDTO toConvocatoria(CoordinacionPersonaProjection projection) {
        if (projection.getIdConvocatoria() == null) {
            return null;
        }
        PeriodoUniversidadDTO periodo = projection.getIdPeriodoUniversidad() == null
                ? null
                : new PeriodoUniversidadDTO(
                        projection.getIdPeriodoUniversidad(),
                        projection.getAnioPeriodo(),
                        projection.getPeriodo());
        return new ConvocatoriaListadoDTO(
                projection.getIdConvocatoria(),
                projection.getNombreConvocatoria(),
                projection.getDescripcionConvocatoria(),
                projection.getEstadoConvocatoria(),
                periodo);
    }

    @Mapping(target = "nombre", ignore = true)
    @Mapping(target = "descripcion", ignore = true)
    @Mapping(target = "codigo", ignore = true)
    CoordinacionBusquedaDTO toBusquedaDto(CoordinacionesEntity entity);

    List<CoordinacionBusquedaDTO> toBusquedaDtoList(List<CoordinacionesEntity> entities);
}
