package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.BusquedaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CategoriaCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EscalafonDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocentePreasignacionProjection;

@Mapper(componentModel = "spring")
public interface BusquedaDocenteMapper {

    default List<BusquedaDocenteDTO> toDtoList(List<DocentePreasignacionProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toDto).toList();
    }

    default BusquedaDocenteDTO toDto(DocentePreasignacionProjection projection) {
        return new BusquedaDocenteDTO(
                projection.getIdPersonaGeneral(),
                projection.getDocumentoIdentidad(),
                nombreCompleto(projection),
                toCategoria(projection),
                toEscalafon(projection),
                null,
                null,
                null,
                null);
    }

    private static CategoriaCatedraticoDTO toCategoria(DocentePreasignacionProjection projection) {
        if (projection.getIdCategoriaCatedratico() == null) {
            return null;
        }
        return new CategoriaCatedraticoDTO(
                projection.getIdCategoriaCatedratico(),
                projection.getDescripcionCategoriaCatedratico());
    }

    private static EscalafonDTO toEscalafon(DocentePreasignacionProjection projection) {
        if (projection.getIdEscalafon() == null) {
            return null;
        }
        return new EscalafonDTO(
                projection.getIdEscalafon(),
                projection.getIdCategoriaCatedratico(),
                projection.getIdModalidadContratacion(),
                projection.getIdPersonaGeneral(),
                projection.getPuntos());
    }

    private static String nombreCompleto(DocentePreasignacionProjection projection) {
        return Stream.of(
                        projection.getPrimerNombre(),
                        projection.getSegundoNombre(),
                        projection.getPrimerApellido(),
                        projection.getSegundoApellido())
                .filter(part -> part != null && !part.isBlank())
                .collect(Collectors.joining(" "));
    }
}
