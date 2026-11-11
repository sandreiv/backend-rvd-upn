/**
 * Aplicación: rvd
 * Archivo: TipoActividadListadoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadArbolProjection;

@Mapper(componentModel = "spring")
public interface TipoActividadListadoMapper {

    default TipoActividadListadoDTO toDto(
            TipoActividadArbolProjection projection,
            List<TipoActividadListadoDTO> hijos) {
        if (projection == null) {
            return null;
        }
        return new TipoActividadListadoDTO(
                projection.getId(),
                projection.getNombre(),
                projection.getDescripcion(),
                projection.getOrden(),
                projection.getEstado(),
                projection.getMinimoHoras(),
                projection.getMaximoHoras(),
                projection.getIdDependencia(),
                projection.getDocenciaDirecta(),
                projection.getComponente(),
                projection.getHorasPorSemestre(),
                hijos == null ? List.of() : hijos);
    }
}

/* 09/11/2026 */
