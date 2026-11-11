/**
 * Aplicación: rvd
 * Archivo: RestriccionActividadListadoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.IdNombreDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionActividadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.RestriccionActividadListadoProjection;

@Mapper(componentModel = "spring")
public interface RestriccionActividadListadoMapper {

    default List<RestriccionActividadListadoDTO> toDtoList(
            List<RestriccionActividadListadoProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default RestriccionActividadListadoDTO toDto(RestriccionActividadListadoProjection projection) {
        if (projection == null) {
            return null;
        }
        IdNombreDTO tipoActividad = projection.getIdTipoActividad() == null
                ? null
                : new IdNombreDTO(projection.getIdTipoActividad(), projection.getNombreTipoActividad());
        return new RestriccionActividadListadoDTO(projection.getId(), tipoActividad);
    }
}

/* 09/11/2026 */
