package co.edu.unipamplona.ciadti.rvd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NivelEducativoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.util.FechasConvocatoriaCalculator;

@Mapper(
        componentModel = "spring",
        uses = PersonaAutorizaConvocatoriaMapper.class)
public interface ConvocatoriaMapper {

    @Mapping(source = "convocatoria.id", target = "id")
    @Mapping(source = "convocatoria.idPeriodoUniversidad", target = "idPeriodoUniversidad")
    @Mapping(source = "convocatoria.descripcion", target = "descripcion")
    @Mapping(source = "convocatoria.nombre", target = "nombre")
    @Mapping(source = "convocatoria.idRelacion", target = "idRelacion")
    @Mapping(source = "convocatoria.esIntersemestral", target = "esIntersemestral")
    @Mapping(source = "convocatoria.fechaInicio", target = "fechaInicio")
    @Mapping(source = "convocatoria.fechaFin", target = "fechaFin")
    @Mapping(target = "contratacion", ignore = true)
    @Mapping(
            source = "periodo",
            target = "periodoUniversidad",
            qualifiedByName = "formatPeriodoUniversidad")
    @Mapping(source = "nivel.descripcion", target = "nivelEducativo")
    @Mapping(
            source = "autoriza.personaNaturalGeneral",
            target = "nombreCompleto",
            qualifiedByName = "buildNombreCompleto")
    @Mapping(
            source = "fechaCnv",
            target = "estado",
            qualifiedByName = "resolveEstadoByFechas")
    ConvocatoriaDTO toListDto(
            ConvocatoriaEntity convocatoria,
            PeriodoUniversidadEntity periodo,
            NivelEducativoEntity nivel,
            FechasConvocatoriaEntity fechaCnv,
            PersonaGeneralEntity autoriza);

    @Named("formatPeriodoUniversidad")
    default String formatPeriodoUniversidad(PeriodoUniversidadEntity periodo) {
        if (periodo == null || periodo.getAno() == null) {
            return "";
        }
        String periodoStr = periodo.getPeriodo() != null
                ? periodo.getPeriodo()
                : "";
        return periodo.getAno() + " - " + periodoStr;
    }

    @Named("resolveEstadoByFechas")
    default String resolveEstadoByFechas(FechasConvocatoriaEntity fechaCnv) {
        if (fechaCnv == null) {
            return "1";
        }
        return FechasConvocatoriaCalculator.resolveEstadoByFechaFin(
                fechaCnv.getFechaFin());
    }
}
