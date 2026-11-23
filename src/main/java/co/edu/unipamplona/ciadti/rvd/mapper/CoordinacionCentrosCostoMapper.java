/**
 * Aplicación: rvd
 * Archivo: CoordinacionCentrosCostoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/11/2026
 * Modificaciones:
 * 19/11/2026 - Agrupa los programas de la coordinación y sus centros de costo.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionCentrosCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaCentrosCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProgramaCentroCostoProjection;

@Mapper(componentModel = "spring")
public interface CoordinacionCentrosCostoMapper {

    default CoordinacionCentrosCostoDTO toDto(
            Long idCoordinacion,
            String nombre,
            List<CatalogoAdministracionProjection> centrosCosto,
            List<ProgramaCentroCostoProjection> programas) {
        return new CoordinacionCentrosCostoDTO(
                idCoordinacion,
                nombre,
                centrosCosto(centrosCosto),
                programas(programas));
    }

    private static List<CentroCostoDTO> centrosCosto(List<CatalogoAdministracionProjection> centrosCosto) {
        if (centrosCosto == null || centrosCosto.isEmpty()) {
            return List.of();
        }
        return centrosCosto.stream()
                .filter(centro -> centro.getId() != null)
                .map(centro -> new CentroCostoDTO(centro.getId(), centro.getLabel()))
                .toList();
    }

    private static List<ProgramaCentrosCostoDTO> programas(List<ProgramaCentroCostoProjection> filas) {
        if (filas == null || filas.isEmpty()) {
            return List.of();
        }
        Map<Long, ProgramaCentrosCostoDTO> agrupados = new LinkedHashMap<>();
        for (ProgramaCentroCostoProjection fila : filas) {
            if (fila.getIdPrograma() == null) {
                continue;
            }
            ProgramaCentrosCostoDTO programa = agrupados.computeIfAbsent(
                    fila.getIdPrograma(),
                    id -> new ProgramaCentrosCostoDTO(id, fila.getNombrePrograma(), fila.getNombreGrupo(), new ArrayList<>()));
            if (fila.getIdCentroCosto() != null && programa.centrosCosto().stream()
                    .noneMatch(centro -> fila.getIdCentroCosto().equals(centro.id()))) {
                programa.centrosCosto().add(
                        new CentroCostoDTO(fila.getIdCentroCosto(), fila.getDescripcionCentroCosto()));
            }
        }
        return List.copyOf(agrupados.values());
    }
}
