/**
 * Aplicación: rvd
 * Archivo: DocenteCargaMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Creación inicial
 * 09/11/2026 - Estado de la carga como objeto de RVDIES.ESTADOS.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocenteCargaProjection;

@Mapper(componentModel = "spring")
public interface DocenteCargaMapper {

    default List<DocenteCargaDTO> toDtoList(List<DocenteCargaProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default DocenteCargaDTO toDto(DocenteCargaProjection projection) {
        return new DocenteCargaDTO(
                projection.getIdPersonaGeneral(),
                projection.getNombreCompleto(),
                projection.getIdCargaDocente(),
                projection.getIdTipoVinculacionConvocatoria(),
                projection.getIdCategoriaCatedratico(),
                projection.getIdCoordinacion(),
                toEstadoCarga(projection),
                toLocalDate(projection.getCargaFechaInicio()),
                toLocalDate(projection.getCargaFechaFin()),
                projection.getValorContrato(),
                projection.getValorPrestaciones(),
                projection.getAsignacionSalarial(),
                projection.getTotalContrato(),
                projection.getHoras(),
                projection.getValorHora(),
                projection.getPuntos(),
                projection.getValorPunto(),
                projection.getSemanas(),
                projection.getOnceMeses(),
                projection.getTieneActividades());
    }

    private static EstadoCargaDTO toEstadoCarga(DocenteCargaProjection projection) {
        if (projection.getIdEstado() == null) {
            return null;
        }
        return new EstadoCargaDTO(
                projection.getIdEstado(),
                projection.getNombreEstado(),
                projection.getDescripcionEstado(),
                projection.getOrdenEstado(),
                projection.getTipoEstado());
    }

    private static LocalDate toLocalDate(Date fecha) {
        if (fecha == null) {
            return null;
        }
        if (fecha instanceof java.sql.Date fechaSql) {
            return fechaSql.toLocalDate();
        }
        return new java.sql.Date(fecha.getTime()).toLocalDate();
    }
}
