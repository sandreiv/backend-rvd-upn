/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Leonel Antonio Perez Rios - Creación inicial
 * 13/11/2026 - Mapeo de centro de costo, programa, funcion y data (extracción de funcion desde JSON)
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaProyectoListaProjection;

@Mapper(componentModel = "spring")
public interface PersonaProyectoMapper {

    default PersonaProyectoListaDTO toPersonaProyectoListaDTO(
            PersonaProyectoListaProjection projection) {
        if (projection == null) {
            return null;
        }

        String data = projection.getData();
        String funcion = extractFuncion(data, projection.getFuncion());

        return new PersonaProyectoListaDTO(
                projection.getId(),
                projection.getIdProyecto(),
                projection.getIdPersonaGeneral(),
                projection.getNombreCompleto(),
                projection.getDocumento(),
                projection.getHoras(),
                projection.getObservacion(),
                projection.getIdCentroCosto(),
                projection.getCentroCostoNombre(),
                projection.getCentroCostoCodigo(),
                projection.getIdPrograma(),
                funcion,
                data,
                projection.getEstado(),
                projection.getEsActivo()
        );
    }

    default List<PersonaProyectoListaDTO> toPersonaProyectoListaDTOList(
            List<PersonaProyectoListaProjection> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        return list.stream().map(this::toPersonaProyectoListaDTO).toList();
    }

    private String extractFuncion(String data, String defaultFuncion) {
        if (data == null || data.isBlank()) {
            return defaultFuncion;
        }

        String trimmed = data.trim();
        if (trimmed.startsWith("{") && trimmed.contains("\"funcion\"")) {
            int idx = trimmed.indexOf("\"funcion\"");
            int colonIdx = trimmed.indexOf(":", idx);
            if (colonIdx != -1) {
                int quoteStart = trimmed.indexOf("\"", colonIdx);
                if (quoteStart != -1) {
                    int quoteEnd = trimmed.indexOf("\"", quoteStart + 1);
                    if (quoteEnd != -1) {
                        return trimmed.substring(quoteStart + 1, quoteEnd);
                    }
                }
            }
        }
        return defaultFuncion != null ? defaultFuncion : trimmed;
    }
}
