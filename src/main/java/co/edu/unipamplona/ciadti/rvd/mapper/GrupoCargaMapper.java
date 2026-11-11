/**
 * Aplicación: rvd
 * Archivo: GrupoCargaMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.GrupoCargaProjection;

@Mapper(componentModel = "spring")
public interface GrupoCargaMapper {

    default List<GrupoDTO> toDtoList(List<GrupoCargaProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default GrupoDTO toDto(GrupoCargaProjection projection) {
        if (projection == null) {
            return null;
        }
        PeriodoUniversidadDTO periodo = projection.getIdPeriodoUniversidad() == null
                ? null
                : new PeriodoUniversidadDTO(
                        projection.getIdPeriodoUniversidad(),
                        projection.getAnio(),
                        projection.getPeriodo(),
                        projection.getCodigoPeriodo());
        return new GrupoDTO(
                projection.getId(),
                projection.getNombre(),
                projection.getCapacidad(),
                periodo);
    }
}
