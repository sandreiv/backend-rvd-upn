/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Leonel Antonio Perez Rios - Creación inicial
 * 13/11/2026 - Mapeo de centro de costo, programa, funcion y data (extracción de funcion desde JSON)
 * 17/11/2026 - Extracción de nivel desde JSON data y parseo robusto con Jackson ObjectMapper
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaProyectoListaProjection;

@Mapper(componentModel = "spring")
public interface PersonaProyectoMapper {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    default PersonaProyectoListaDTO toPersonaProyectoListaDTO(
            PersonaProyectoListaProjection projection) {
        if (projection == null) {
            return null;
        }

        String data = extractStringData(projection.getData());
        String funcion = extractFuncion(data, projection.getFuncion());
        String nivel = extractField(data, "nivel");

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
                nivel,
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
        String extracted = extractField(data, "funcion");
        if (extracted != null && !extracted.isBlank()) {
            return extracted;
        }
        if (defaultFuncion != null && !defaultFuncion.isBlank()) {
            return defaultFuncion;
        }
        if (data != null && !data.trim().startsWith("{")) {
            return data.trim();
        }
        return null;
    }

    private String extractField(String data, String field) {
        if (data == null || data.isBlank()) {
            return null;
        }
        String trimmed = data.trim();
        if (!trimmed.startsWith("{")) {
            return null;
        }
        try {
            JsonNode root = OBJECT_MAPPER.readTree(trimmed);
            JsonNode node = root.get(field);
            return (node != null && !node.isNull()) ? node.asText() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String extractStringData(Object rawData) {
        if (rawData == null) {
            return null;
        }
        if (rawData instanceof String str) {
            return str;
        }
        if (rawData instanceof java.sql.Clob clob) {
            try {
                long len = clob.length();
                if (len <= 0) {
                    return null;
                }
                return clob.getSubString(1, (int) Math.min(len, Integer.MAX_VALUE));
            } catch (Exception e) {
                return null;
            }
        }
        return rawData.toString();
    }
}