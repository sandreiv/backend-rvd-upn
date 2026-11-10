/**
 * Aplicación: rvd
 * Archivo: ProyectosMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Leonel Antonio Perez Rios - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.IdNombreDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProyectosListaProjection;

@Mapper(componentModel = "spring")
public interface ProyectosMapper {

    default ProyectosListaDTO toProyectosListaDTO(ProyectosListaProjection projection) {
        if (projection == null) {
            return null;
        }
        return new ProyectosListaDTO(
                projection.getId(),
                projection.getIdTipoActividad(),
                projection.getIdCoordinacion(),
                projection.getIdProyectoPadre(),
                projection.getIdMateriaTdg(),
                projection.getNombre(),
                projection.getDescripcion(),
                projection.getActaConsejo(),
                projection.getEstado(),
                toIdNombre(
                        projection.getIdTipoActividad(),
                        projection.getNombreTipoActividad()),
                toIdNombre(
                        projection.getIdCoordinacion(),
                        projection.getNombreCoordinacion()),
                projection.getTotalDocentes()
        );
    }

    default List<ProyectosListaDTO> toProyectosListaDTOList(
            List<ProyectosListaProjection> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        return list.stream().map(this::toProyectosListaDTO).toList();
    }

    default IdNombreDTO toIdNombre(Long id, String nombre) {
        if (id == null) {
            return null;
        }
        return new IdNombreDTO(id, nombre != null ? nombre : "");
    }
}
