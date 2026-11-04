package co.edu.unipamplona.ciadti.rvd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;

@Mapper(componentModel = "spring")
public interface ConvocatoriaMapper {

    @Mapping(source = "convocatoria.id", target = "id")
    @Mapping(source = "convocatoria.nombre", target = "nombre")
    @Mapping(source = "convocatoria.descripcion", target = "descripcion")
    @Mapping(source = "convocatoria.idPeriodoUniversidad", target = "idPeriodoUniversidad")
    @Mapping(source = "convocatoria.periodoUniversidad", target = "periodoUniversidad", qualifiedByName = "formatPeriodoUniversidad")
    @Mapping(source = "convocatoria.fechaInicio", target = "fechaInicio")
    @Mapping(source = "convocatoria.fechaFin", target = "fechaFin")
    @Mapping(source = "convocatoria.estado", target = "estado")
    @Mapping(source = "convocatoria.idRelacion", target = "idRelacion")
    @Mapping(source = "convocatoria.esIntersemestral", target = "esIntersemestral")
    ConvocatoriaDTO toListDto(ConvocatoriaEntity convocatoria);

    @Named("formatPeriodoUniversidad")
    default String formatPeriodoUniversidad(PeriodoUniversidadEntity periodo) {
        if (periodo == null || periodo.getAno() == null) {
            return "";
        }
        String periodoStr = periodo.getPeriodo() != null ? periodo.getPeriodo() : "";
        return periodo.getAno() + " - " + periodoStr;
    }
}