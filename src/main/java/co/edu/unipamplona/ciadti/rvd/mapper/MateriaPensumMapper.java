/**
 * Aplicación: rvd
 * Archivo: MateriaPensumMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Creación inicial.
 * 11/11/2026 - Horas directas como suma de prácticas y teóricas.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaPensumProjection;

@Mapper(componentModel = "spring")
public interface MateriaPensumMapper {

    default List<MateriaDTO> toDtoList(List<MateriaPensumProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default MateriaDTO toDto(MateriaPensumProjection projection) {
        if (projection == null) {
            return null;
        }
        return new MateriaDTO(
                projection.getCodigoMateria(),
                projection.getNombre(),
                projection.getHorasPracticas(),
                projection.getHorasTeoricas(),
                projection.getHorasTeoricoPracticas(),
                projection.getPeriodo(),
                projection.getPonderacionAcademica(),
                calcularHorasDirectas(projection),
                projection.getCapacidad(),
                flag(projection.getTieneGrupo()),
                flag(projection.getEsElectiva()),
                null,
                flag(projection.getEsCompartida()));
    }

    default Long calcularHorasDirectas(MateriaPensumProjection projection) {
        long practicas = projection.getHorasPracticas() != null
                ? projection.getHorasPracticas() : 0L;
        long teoricas = projection.getHorasTeoricas() != null
                ? projection.getHorasTeoricas() : 0L;
        return practicas + teoricas;
    }

    private static Boolean flag(Long value) {
        return value != null && value == 1L;
    }
}
