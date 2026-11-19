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
 * 17/11/2026 - Proyectos de la persona de una carga docente.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HorarioProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaParticularDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaProyectoCargaProjection;
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

    default List<PersonaProyectoDTO> toDtoList(List<PersonaProyectoCargaProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default PersonaProyectoDTO toDto(PersonaProyectoCargaProjection projection) {
        if (projection == null) {
            return null;
        }
        String data = texto(projection.getData());
        JsonNode json = leer(data);
        return new PersonaProyectoDTO(
                projection.getId(),
                projection.getIdPersonaGeneral(),
                projection.getIdPrograma(),
                projection.getIdCentroCosto(),
                projection.getHoras(),
                projection.getObservacion(),
                new ProyectoDTO(
                        projection.getIdProyecto(),
                        projection.getIdProyectoPadre(),
                        tipoActividad(projection),
                        projection.getIdCoordinacion(),
                        materiaParticular(projection),
                        projection.getIdProyectoGrupo(),
                        projection.getNombre(),
                        projection.getDescripcion(),
                        projection.getActaConsejo(),
                        projection.getNumeroActa(),
                        projection.getFechaActa(),
                        projection.getCodigo(),
                        projection.getActivo()),
                campo(json, "funcion"),
                campo(json, "nivel"),
                horario(json));
    }

    private static TipoActividadDTO tipoActividad(PersonaProyectoCargaProjection projection) {
        if (projection.getIdTipoActividad() == null) {
            return null;
        }
        return new TipoActividadDTO(
                projection.getIdTipoActividad(),
                projection.getIdTipoActividadPadre(),
                projection.getNombreTipoActividad(),
                projection.getDescripcionTipoActividad(),
                projection.getOrdenTipoActividad(),
                projection.getEstadoTipoActividad(),
                projection.getMinimoHorasTipoActividad(),
                projection.getMaximoHorasTipoActividad(),
                projection.getIdDependenciaTipoActividad(),
                projection.getDocenciaDirectaTipoActividad(),
                projection.getComponenteTipoActividad(),
                projection.getHorasPorSemestreTipoActividad(),
                projection.getAplicaProyectosTipoActividad());
    }

    private static MateriaParticularDTO materiaParticular(PersonaProyectoCargaProjection projection) {
        if (projection.getIdMateriaParticular() == null) {
            return null;
        }
        Long practicas = projection.getHorasPracticasMateriaParticular();
        Long teoricas = projection.getHorasTeoricasMateriaParticular();
        long horasDirectas = (practicas == null ? 0L : practicas) + (teoricas == null ? 0L : teoricas);
        boolean compartida = projection.getTipoMateriaParticular() != null
                && "MATERIACOMPARTIDA".equalsIgnoreCase(projection.getTipoMateriaParticular().trim());
        MateriaDTO materia = projection.getCodigoMateriaParticular() == null
                && projection.getNombreMateriaParticular() == null
                        ? null
                        : new MateriaDTO(
                                projection.getCodigoMateriaParticular(),
                                projection.getNombreMateriaParticular(),
                                practicas,
                                teoricas,
                                projection.getHorasTeoricoPracticasMateriaParticular(),
                                null,
                                projection.getPonderacionMateriaParticular(),
                                horasDirectas,
                                projection.getCapacidadMateriaParticular(),
                                flag(projection.getTieneGrupoMateriaParticular()),
                                flag(projection.getEsElectivaMateriaParticular()),
                                null,
                                compartida,
                                projection.getDocentesPorGrupoMateriaParticular());
        ProgramaDTO programa = projection.getIdProgramaMateriaParticular() == null
                ? null
                : new ProgramaDTO(
                        projection.getIdProgramaMateriaParticular(),
                        projection.getNombreProgramaMateriaParticular(),
                        projection.getIdCentroCostoProgramaMateriaParticular() == null
                                ? null
                                : new CentroCostoDTO(
                                        projection.getIdCentroCostoProgramaMateriaParticular(),
                                        projection.getDescripcionCentroCostoProgramaMateriaParticular()));
        return new MateriaParticularDTO(
                projection.getIdMateriaParticular(),
                materia,
                programa,
                projection.getTipoMateriaParticular(),
                projection.getDocentesPorGrupoMateriaParticular());
    }

    private static Boolean flag(Long value) {
        return value != null && value == 1L;
    }

    private static JsonNode leer(String data) {
        if (data == null || data.isBlank() || !data.trim().startsWith("{")) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(data.trim());
        } catch (Exception ex) {
            return null;
        }
    }

    private static String campo(JsonNode json, String nombre) {
        if (json == null) {
            return null;
        }
        JsonNode nodo = json.get(nombre);
        if (nodo == null || nodo.isNull() || !nodo.isTextual() || nodo.asText().isBlank()) {
            return null;
        }
        return nodo.asText();
    }

    private static List<HorarioProyectoDTO> horario(JsonNode json) {
        if (json == null) {
            return List.of();
        }
        JsonNode nodos = json.get("horario");
        if (nodos == null || !nodos.isArray()) {
            return List.of();
        }
        List<HorarioProyectoDTO> horarios = new ArrayList<>();
        for (JsonNode nodo : nodos) {
            if (nodo == null || !nodo.isObject()) {
                continue;
            }
            horarios.add(new HorarioProyectoDTO(
                    campo(nodo, "dia"),
                    campo(nodo, "horaInicio"),
                    campo(nodo, "horaFin")));
        }
        return horarios;
    }

    private static String texto(Clob clob) {
        if (clob == null) {
            return null;
        }
        try {
            long length = clob.length();
            if (length == 0) {
                return "";
            }
            int size = length > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) length;
            return clob.getSubString(1, size);
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo leer PEPR_DATA.", ex);
        }
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
