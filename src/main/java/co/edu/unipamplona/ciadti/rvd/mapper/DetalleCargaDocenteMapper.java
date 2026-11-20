/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Listado de detalle de carga docente.
 * 12/11/2026 - tipoActividad es la hoja guardada en TIAC_ID, con su idPadre.
 * 12/11/2026 - Pensum del programa que contiene la materia.
 * 17/11/2026 - tipoDocente leído de DECD_DATA.
 * 19/11/2026 - termino y actoAdministrativo leídos de DECD_DATA.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mapstruct.Mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ActoAdministrativoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HorarioClaseDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDetalleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PensumDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TerminoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleCargaDocenteCargaProjection;

@Mapper(componentModel = "spring")
public interface DetalleCargaDocenteMapper {

    ObjectMapper JSON = new ObjectMapper();
    String TITULAR = "TITULAR";

    default List<Long> idsClaseSemanal(List<DetalleCargaDocenteCargaProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        Set<Long> ids = new LinkedHashSet<>();
        for (DetalleCargaDocenteCargaProjection projection : projections) {
            ids.addAll(idsClaseSemanal(texto(projection.getData())));
        }
        return List.copyOf(ids);
    }

    default List<DetalleCargaDocenteDTO> toDtoList(
            List<DetalleCargaDocenteCargaProjection> projections,
            Map<Long, HorarioClaseDTO> clasesSemanales) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        Map<Long, HorarioClaseDTO> horarios = clasesSemanales == null ? Map.of() : clasesSemanales;
        Map<Long, DetalleCargaDocenteDTO> porDetalle = new LinkedHashMap<>();
        List<DetalleCargaDocenteDTO> sinId = new ArrayList<>();
        for (DetalleCargaDocenteCargaProjection projection : projections) {
            DetalleCargaDocenteDTO dto = toDto(projection, horarios);
            if (dto.idDetalleCargaDocente() == null) {
                sinId.add(dto);
                continue;
            }
            porDetalle.putIfAbsent(dto.idDetalleCargaDocente(), dto);
        }
        List<DetalleCargaDocenteDTO> result = new ArrayList<>(porDetalle.values());
        result.addAll(sinId);
        return result;
    }

    default DetalleCargaDocenteDTO toDto(
            DetalleCargaDocenteCargaProjection projection,
            Map<Long, HorarioClaseDTO> clasesSemanales) {
        if (projection == null) {
            return null;
        }
        return new DetalleCargaDocenteDTO(
                projection.getIdDetalleCargaDocente(),
                projection.getIdCargaDocente(),
                List.of(toActividad(projection, clasesSemanales)));
    }

    private static DetalleCargaDocenteActividadDTO toActividad(
            DetalleCargaDocenteCargaProjection projection,
            Map<Long, HorarioClaseDTO> clasesSemanales) {
        TipoActividadDTO actividad = tipoActividad(projection);
        String data = texto(projection.getData());
        return new DetalleCargaDocenteActividadDTO(
                actividad,
                projection.getIdUnidadRegional() == null
                        ? null
                        : new UnidadDTO(projection.getIdUnidadRegional(), projection.getNombreUnidadRegional()),
                projection.getIdPrograma() == null
                        ? null
                        : new ProgramaDTO(projection.getIdPrograma(), projection.getNombrePrograma(), null),
                materia(projection),
                projection.getIdGrupo() == null
                        ? null
                        : new GrupoDTO(
                                projection.getIdGrupo(),
                                projection.getNombreGrupo(),
                                projection.getCapacidadGrupo(),
                                null),
                projection.getIdCentroCosto() == null
                        ? null
                        : new CentroCostoDTO(projection.getIdCentroCosto(), projection.getDescripcionCentroCosto()),
                projection.getHoras(),
                pensum(projection),
                materiaBanco(projection),
                projection.getExtras(),
                projection.getDescripcion(),
                clases(data, clasesSemanales),
                tipoDocente(data),
                termino(data),
                actoAdministrativo(data));
    }

    private static String tipoDocente(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            JsonNode nodo = JSON.readTree(json).get("tipoDocente");
            if (nodo == null || !nodo.isTextual() || !TITULAR.equals(nodo.asText())) {
                return null;
            }
            return TITULAR;
        } catch (Exception ex) {
            return null;
        }
    }

    private static TerminoDTO termino(String json) {
        JsonNode nodo = objeto(json, "termino");
        if (nodo == null) {
            return null;
        }
        Date desde = fecha(nodo.get("desde"));
        Date hasta = fecha(nodo.get("hasta"));
        if (desde == null && hasta == null) {
            return null;
        }
        return new TerminoDTO(desde, hasta);
    }

    private static ActoAdministrativoDTO actoAdministrativo(String json) {
        JsonNode nodo = objeto(json, "actoAdministrativo");
        if (nodo == null) {
            return null;
        }
        Long numero = numero(nodo.get("numero"));
        Date fecha = fecha(nodo.get("fecha"));
        if (numero == null && fecha == null) {
            return null;
        }
        return new ActoAdministrativoDTO(numero, fecha);
    }

    private static JsonNode objeto(String json, String campo) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            JsonNode nodo = JSON.readTree(json).get(campo);
            return nodo != null && nodo.isObject() ? nodo : null;
        } catch (Exception ex) {
            return null;
        }
    }

    private static Date fecha(JsonNode nodo) {
        if (nodo == null || nodo.isNull() || !nodo.isTextual() || nodo.asText().isBlank()) {
            return null;
        }
        try {
            return Date.valueOf(nodo.asText().trim());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private static Long numero(JsonNode nodo) {
        if (nodo == null || nodo.isNull()) {
            return null;
        }
        if (nodo.isNumber()) {
            return nodo.longValue();
        }
        if (!nodo.isTextual() || nodo.asText().isBlank()) {
            return null;
        }
        try {
            return Long.valueOf(nodo.asText().trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static List<HorarioClaseDTO> clases(String json, Map<Long, HorarioClaseDTO> clasesSemanales) {
        List<HorarioClaseDTO> clases = new ArrayList<>();
        for (Long id : idsClaseSemanal(json)) {
            HorarioClaseDTO clase = clasesSemanales.get(id);
            if (clase != null) {
                clases.add(clase);
            }
        }
        return clases;
    }

    private static List<Long> idsClaseSemanal(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            JsonNode nodos = JSON.readTree(json).get("idsClaseSemanal");
            if (nodos == null || !nodos.isArray()) {
                return List.of();
            }
            List<Long> ids = new ArrayList<>();
            for (JsonNode nodo : nodos) {
                if (nodo != null && nodo.isNumber()) {
                    ids.add(nodo.longValue());
                }
            }
            return ids;
        } catch (Exception ex) {
            return List.of();
        }
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
            throw new IllegalStateException("No se pudo leer DECD_DATA.", ex);
        }
    }

    private static PensumDTO pensum(DetalleCargaDocenteCargaProjection projection) {
        if (projection.getIdPensum() == null) {
            return null;
        }
        return new PensumDTO(
                projection.getIdPensum(),
                projection.getDescripcionPensum(),
                projection.getAnioInicioPensum(),
                projection.getPeriodoInicioPensum(),
                projection.getNumPeriodosPensum());
    }

    private static TipoActividadDTO tipoActividad(DetalleCargaDocenteCargaProjection projection) {
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

    private static MateriaDetalleDTO materia(DetalleCargaDocenteCargaProjection projection) {
        if (projection.getCodigoMateria() == null && projection.getNombreMateria() == null) {
            return null;
        }
        return new MateriaDetalleDTO(
                projection.getCodigoMateria(),
                projection.getNombreMateria(),
                projection.getPonderacionAcademica());
    }

    private static MateriaDetalleDTO materiaBanco(DetalleCargaDocenteCargaProjection projection) {
        if (projection.getCodigoMateriaBanco() == null && projection.getNombreMateriaBanco() == null) {
            return null;
        }
        return new MateriaDetalleDTO(
                projection.getCodigoMateriaBanco(),
                projection.getNombreMateriaBanco(),
                projection.getPonderacionAcademicaBanco());
    }
}
