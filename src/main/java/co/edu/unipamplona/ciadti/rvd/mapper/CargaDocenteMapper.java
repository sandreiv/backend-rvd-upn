/**
 * Aplicación: rvd
 * Archivo: CargaDocenteMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 01/07/2026
 * Modificaciones:
 * 01/07/2026 - Sebastian Jaimes - Creación inicial
 * 17/09/2026 - Sebastian Jaimes - Ignora valores de contrato enviados por el cliente
 * 06/10/2026 - Ignora creadoEnNovedad (lo asigna manualmente el flujo de novedades)
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocentePlantaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;

@Mapper(componentModel = "spring")
public interface CargaDocenteMapper {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "salario", source = "asignacionSalarial")
    @Mapping(target = "valorContrato", ignore = true)
    @Mapping(target = "valorPrestaciones", ignore = true)
    @Mapping(target = "totalContrato", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "vigente", ignore = true)
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "fechaCambio", ignore = true)
    @Mapping(target = "horas", ignore = true)
    @Mapping(target = "onceMeses", ignore = true)
    CargaDocenteEntity toEntity(CargaDocenteFormularioDTO dto);

    @InheritConfiguration(name = "toEntity")
    void updateEntity(CargaDocenteFormularioDTO dto, @MappingTarget CargaDocenteEntity entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idCategoriaCatedratico", ignore = true)
    CargaDocenteEntity toEntityFromPlanta(CargaDocentePlantaDTO dto);
}
