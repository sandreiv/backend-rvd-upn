/**
 * Aplicación: rvd
 * Archivo: MateriaComponenteBancoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaComponenteBancoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaComponenteBancoProjection;

@Mapper(componentModel = "spring")
public interface MateriaComponenteBancoMapper {

    default List<MateriaComponenteBancoDTO> toDtoList(List<MateriaComponenteBancoProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default MateriaComponenteBancoDTO toDto(MateriaComponenteBancoProjection projection) {
        if (projection == null) {
            return null;
        }
        MateriaDTO materia = new MateriaDTO(
                projection.getCodigoMateria(),
                projection.getNombre(),
                projection.getHorasPracticas(),
                projection.getHorasTeoricas(),
                projection.getHorasTeoricoPracticas(),
                null,
                projection.getPonderacionAcademica(),
                calcularHorasDirectas(projection),
                projection.getCapacidad(),
                null,
                false,
                null);
        return new MateriaComponenteBancoDTO(materia, projection.getPeso(), projection.getEstado());
    }

    default Long calcularHorasDirectas(MateriaComponenteBancoProjection projection) {
        long practicas = projection.getHorasPracticas() != null
                ? projection.getHorasPracticas() : 0L;
        long teoricas = projection.getHorasTeoricas() != null
                ? projection.getHorasTeoricas() : 0L;
        return practicas + teoricas;
    }
}
