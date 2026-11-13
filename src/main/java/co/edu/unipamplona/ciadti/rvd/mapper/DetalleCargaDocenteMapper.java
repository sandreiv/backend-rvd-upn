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
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDetalleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PensumDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleCargaDocenteCargaProjection;

@Mapper(componentModel = "spring")
public interface DetalleCargaDocenteMapper {

    default List<DetalleCargaDocenteDTO> toDtoList(List<DetalleCargaDocenteCargaProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        Map<Long, DetalleCargaDocenteDTO> porDetalle = new LinkedHashMap<>();
        List<DetalleCargaDocenteDTO> sinId = new ArrayList<>();
        for (DetalleCargaDocenteCargaProjection projection : projections) {
            DetalleCargaDocenteDTO dto = toDto(projection);
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

    default DetalleCargaDocenteDTO toDto(DetalleCargaDocenteCargaProjection projection) {
        if (projection == null) {
            return null;
        }
        return new DetalleCargaDocenteDTO(
                projection.getIdDetalleCargaDocente(),
                projection.getIdCargaDocente(),
                List.of(toActividad(projection)));
    }

    private static DetalleCargaDocenteActividadDTO toActividad(DetalleCargaDocenteCargaProjection projection) {
        TipoActividadDTO actividad = tipoActividad(
                projection.getIdTipoActividad(),
                projection.getIdTipoActividadPadre(),
                projection.getNombreTipoActividad(),
                projection.getDescripcionTipoActividad(),
                projection.getOrdenTipoActividad(),
                projection.getComponenteTipoActividad());
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
                materiaBanco(projection));
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

    private static TipoActividadDTO tipoActividad(
            Long id,
            Long idPadre,
            String nombre,
            String descripcion,
            String orden,
            String componente) {
        if (id == null) {
            return null;
        }
        return new TipoActividadDTO(id, idPadre, nombre, descripcion, orden, null, componente);
    }

    private static MateriaDetalleDTO materia(DetalleCargaDocenteCargaProjection projection) {
        if (projection.getCodigoMateria() == null && projection.getNombreMateria() == null) {
            return null;
        }
        return new MateriaDetalleDTO(projection.getCodigoMateria(), projection.getNombreMateria());
    }

    private static MateriaDetalleDTO materiaBanco(DetalleCargaDocenteCargaProjection projection) {
        if (projection.getCodigoMateriaBanco() == null && projection.getNombreMateriaBanco() == null) {
            return null;
        }
        return new MateriaDetalleDTO(projection.getCodigoMateriaBanco(), projection.getNombreMateriaBanco());
    }
}
