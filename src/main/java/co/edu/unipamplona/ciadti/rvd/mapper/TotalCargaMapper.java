/**
 * Aplicación: rvd
 * Archivo: TotalCargaMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 26/11/2026
 * Modificaciones:
 * 26/11/2026 - Horas de la carga por tipo de actividad.
 * 26/11/2026 - Árbol padre-hijo. Las horas de cada nodo incluyen su subárbol.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.TotalHorasCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TotalHorasCargaProjection;

@Mapper(componentModel = "spring")
public interface TotalCargaMapper {

    default List<TotalHorasCargaDTO> toArbol(List<TotalHorasCargaProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }

        Map<Long, TotalHorasCargaProjection> nodos = new LinkedHashMap<>();
        for (TotalHorasCargaProjection projection : projections) {
            if (projection == null || projection.getIdTipoActividad() == null) {
                continue;
            }
            nodos.putIfAbsent(projection.getIdTipoActividad(), projection);
        }
        if (nodos.isEmpty()) {
            return List.of();
        }

        Map<Long, List<TotalHorasCargaProjection>> porPadre = new LinkedHashMap<>();
        List<TotalHorasCargaProjection> raices = new ArrayList<>();
        for (TotalHorasCargaProjection nodo : nodos.values()) {
            Long idPadre = nodo.getIdPadre();
            if (idPadre == null || !nodos.containsKey(idPadre)) {
                raices.add(nodo);
            } else {
                porPadre.computeIfAbsent(idPadre, key -> new ArrayList<>()).add(nodo);
            }
        }

        List<TotalHorasCargaDTO> arbol = new ArrayList<>();
        for (TotalHorasCargaProjection raiz : raices) {
            arbol.add(toNodo(raiz, porPadre, new HashSet<>()));
        }
        return List.copyOf(arbol);
    }

    private TotalHorasCargaDTO toNodo(
            TotalHorasCargaProjection nodo,
            Map<Long, List<TotalHorasCargaProjection>> porPadre,
            Set<Long> visitados) {
        if (nodo.getIdTipoActividad() == null || !visitados.add(nodo.getIdTipoActividad())) {
            return new TotalHorasCargaDTO(
                    nodo.getIdTipoActividad(),
                    nodo.getNombre(),
                    BigDecimal.ZERO,
                    List.of());
        }

        List<TotalHorasCargaDTO> hijos = new ArrayList<>();
        BigDecimal total = horas(nodo.getHoras());
        for (TotalHorasCargaProjection hijo : porPadre.getOrDefault(nodo.getIdTipoActividad(), List.of())) {
            TotalHorasCargaDTO hijoDto = toNodo(hijo, porPadre, visitados);
            hijos.add(hijoDto);
            total = total.add(horas(hijoDto.horas()));
        }
        return new TotalHorasCargaDTO(
                nodo.getIdTipoActividad(),
                nodo.getNombre(),
                total,
                List.copyOf(hijos));
    }

    private static BigDecimal horas(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
