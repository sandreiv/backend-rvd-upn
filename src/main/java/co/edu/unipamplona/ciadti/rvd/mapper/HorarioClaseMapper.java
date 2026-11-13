/**
 * Aplicación: rvd
 * Archivo: HorarioClaseMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/11/2026
 * Modificaciones:
 * 13/11/2026 - Horario de clase con bloques inicial y final.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.BloqueHoraDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HorarioClaseDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorarioClaseProjection;

@Mapper(componentModel = "spring")
public interface HorarioClaseMapper {

    default List<HorarioClaseDTO> toDtoList(List<HorarioClaseProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default HorarioClaseDTO toDto(HorarioClaseProjection projection) {
        if (projection == null) {
            return null;
        }
        return new HorarioClaseDTO(
                projection.getId(),
                projection.getDia(),
                projection.getFechaInicio(),
                projection.getFechaFinal(),
                bloque(
                        projection.getIdBloqueInicial(),
                        projection.getNombreBloqueInicial(),
                        projection.getHoraInicioBloqueInicial(),
                        projection.getHoraFinalBloqueInicial()),
                bloque(
                        projection.getIdBloqueFinal(),
                        projection.getNombreBloqueFinal(),
                        projection.getHoraInicioBloqueFinal(),
                        projection.getHoraFinalBloqueFinal()));
    }

    private static BloqueHoraDTO bloque(Long id, String nombre, String horaInicio, String horaFinal) {
        if (id == null) {
            return null;
        }
        return new BloqueHoraDTO(id, nombre, horaInicio, horaFinal);
    }
}
