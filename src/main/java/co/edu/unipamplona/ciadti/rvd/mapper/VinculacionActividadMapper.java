/**
 * Aplicación: rvd
 * Archivo: VinculacionActividadMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Juan Diego Florez Vera - Creación inicial para administración de tablas básicas
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.OpcionAgrupadaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.VinculacionActividadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.IdNombreGrupoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.VinculacionActividadProjection;

@Mapper(componentModel = "spring")
public interface VinculacionActividadMapper {

    VinculacionActividadListadoDTO toListadoDTO(
            VinculacionActividadProjection projection
    );

    List<VinculacionActividadListadoDTO> toListadoDTOList(
            List<VinculacionActividadProjection> list
    );

    OpcionAgrupadaDTO toOpcionDTO(
            IdNombreGrupoProjection projection
    );

    List<OpcionAgrupadaDTO> toOpcionDTOList(
            List<IdNombreGrupoProjection> list
    );
}
