/**
 * Aplicación: rvd
 * Archivo: MateriaProgramaCargaMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 * Modificaciones:
 * 17/11/2026 - Materia de la carga con programa y centro de costo.
 * 17/11/2026 - Grupo de la materia.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDetalleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaProgramaCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaProgramaCargaProjection;

@Mapper(componentModel = "spring")
public interface MateriaProgramaCargaMapper {

    default List<MateriaProgramaCargaDTO> toDtoList(List<MateriaProgramaCargaProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default MateriaProgramaCargaDTO toDto(MateriaProgramaCargaProjection projection) {
        if (projection == null) {
            return null;
        }
        CentroCostoDTO centroCosto = projection.getIdCentroCosto() == null
                ? null
                : new CentroCostoDTO(projection.getIdCentroCosto(), projection.getDescripcionCentroCosto());
        return new MateriaProgramaCargaDTO(
                new MateriaDetalleDTO(
                        projection.getCodigoMateria(),
                        projection.getNombreMateria(),
                        projection.getPonderacionAcademica()),
                projection.getIdPrograma() == null
                        ? null
                        : new ProgramaDTO(projection.getIdPrograma(), projection.getNombrePrograma(), centroCosto),
                projection.getIdGrupo() == null
                        ? null
                        : new GrupoDTO(
                                projection.getIdGrupo(),
                                projection.getNombreGrupo(),
                                projection.getCapacidadGrupo(),
                                null));
    }
}
