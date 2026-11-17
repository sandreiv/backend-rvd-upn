/**
 * Aplicación: rvd
 * Archivo: TipoActividadesServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 13/10/2026 - Andrés Hernández - Manejo de campos nulos para actividades PTD
 * 10/11/2026 - Leonel Antonio Perez Rios - Ajuste de validaciones y eliminación para esquema general
 * 10/11/2026 - Leonel Antonio Perez Rios - Soporte para listado completo con idPadre
 * 10/11/2026 - Leonel Antonio Perez Rios - Depuración de campos inexistentes en esquema (codigo, ptd) y ajuste de DTOs
 * 10/11/2026 - Leonel Antonio Perez Rios - Soporte para orden personalizado en creación y edición
 * 10/11/2026 - Leonel Antonio Perez Rios - Mapeo de idDependencia, docenciaDirecta y horasPorSemestre
 * 10/11/2026 - Leonel Antonio Perez Rios - Refactorización de constantes, eliminación de duplicidad y código limpio
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDetalleDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoActividadesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadDetalleProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.TipoActividadesService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TipoActividadesServiceImpl implements TipoActividadesService {

    private static final String ESTADO_ACTIVO = "1";
    private static final String ESTADO_INACTIVO = "0";
    private static final String VALOR_DEFECTO_CERO = "0";

    private final TipoActividadesRepository tipoActividadesRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadDetalleDTO> listParentActivityTypes() {

        List<TipoActividadDetalleDTO> result = tipoActividadesRepository.findAdministrationParentList()
                .stream()
                .map(this::mapToDto)
                .toList();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadDetalleDTO> listAllActivityTypes() {

        List<TipoActividadDetalleDTO> result = tipoActividadesRepository.findAdministrationAllList()
                .stream()
                .map(this::mapToDto)
                .toList();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadDetalleDTO> listChildActivityTypes(Long idPadre) {

        if (idPadre == null || !tipoActividadesRepository.existsById(idPadre)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el tipo de actividad padre");
        }

        List<TipoActividadDetalleDTO> result = tipoActividadesRepository.findAdministrationChildrenList(idPadre)
                .stream()
                .map(this::mapToDto)
                .toList();
        return result;
    }

    @Override
    @Transactional
    public void saveActivityType(TipoActividadFormularioDTO dto) {

        saveActivityTypeWithParent(null, dto);
    }

    @Override
    @Transactional
    public void saveChildActivityType(Long idPadre, TipoActividadFormularioDTO dto) {

        if (idPadre == null || !tipoActividadesRepository.existsById(idPadre)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el tipo de actividad padre");
        }

        saveActivityTypeWithParent(idPadre, dto);
    }

    @Override
    @Transactional
    public void updateActivityType(Long id, TipoActividadFormularioDTO dto) {

        validateActivityType(dto);

        TipoActividadesEntity entity = tipoActividadesRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("updateActivityType ===> Tipo de actividad no encontrado. id={}", id);
                    return new ApiException(
                            HttpStatus.NOT_FOUND,
                            "No existe el tipo de actividad con id " + id
                    );
                });

        fillActivityType(entity, dto);
        tipoActividadesRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteActivityType(Long id) {

        TipoActividadesEntity entity = tipoActividadesRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("deleteActivityType ===> Tipo de actividad no encontrado. id={}", id);
                    return new ApiException(
                            HttpStatus.NOT_FOUND,
                            "No existe el tipo de actividad con id " + id
                    );
                });

        if (tipoActividadesRepository.existsByIdPadre(entity.getId())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede eliminar el tipo de actividad porque tiene actividades hijas asociadas"
            );
        }

        try {
            BigDecimal result = tipoActividadesRepository.deleteByProcedure(
                    id,
                    RegistradoPorUtils.value(Accion.DELETE)
            );

            if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
                tipoActividadesRepository.delete(entity);
            }
        } catch (Exception e) {
            log.warn("deleteActivityType ===> Error en procedimiento, eliminando por repositorio: {}", e.getMessage());
            tipoActividadesRepository.delete(entity);
        }
    }

    @Override
    @Transactional
    public void deleteBulkActivityTypes(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return;
        }

        for (Long id : ids) {
            deleteActivityType(id);
        }
    }

    private void saveActivityTypeWithParent(
            Long idPadre,
            TipoActividadFormularioDTO dto
    ) {
        validateActivityType(dto);

        TipoActividadesEntity entity = new TipoActividadesEntity();
        entity.setIdPadre(idPadre);

        String orden = StringUtils.hasText(dto.orden())
                ? dto.orden().trim()
                : tipoActividadesRepository.findNextOrderByParent(idPadre);
        entity.setOrden(orden);

        fillActivityType(entity, dto);
        tipoActividadesRepository.save(entity);
    }

    private TipoActividadDetalleDTO mapToDto(TipoActividadDetalleProjection item) {
        return new TipoActividadDetalleDTO(
                item.getId(),
                item.getIdPadre(),
                item.getIdDependencia(),
                item.getOrden(),
                item.getNombre(),
                item.getDescripcion(),
                item.getComponente(),
                item.getMinimoHoras(),
                item.getMaximoHoras(),
                item.getDocenciaDirecta(),
                item.getHorasPorSemestre(),
                item.getAplicaProyectos(),
                item.getEstado()
        );
    }

    private void fillActivityType(
            TipoActividadesEntity entity,
            TipoActividadFormularioDTO dto
    ) {
        entity.setNombre(dto.nombre().trim());
        entity.setDescripcion(dto.descripcion() != null ? dto.descripcion().trim() : null);
        entity.setComponente(
                StringUtils.hasText(dto.componente())
                        ? dto.componente().trim()
                        : null);

        if (StringUtils.hasText(dto.orden())) {
            entity.setOrden(dto.orden().trim());
        }

        entity.setIdDependencia(dto.idDependencia());
        entity.setDocenciaDirecta(
                StringUtils.hasText(dto.docenciaDirecta())
                        ? dto.docenciaDirecta().trim()
                        : VALOR_DEFECTO_CERO);
        entity.setHorasPorSemestre(
                StringUtils.hasText(dto.horasPorSemestre())
                        ? dto.horasPorSemestre().trim()
                        : VALOR_DEFECTO_CERO);
        entity.setAplicaProyectos(
                StringUtils.hasText(dto.aplicaProyectos())
                        ? dto.aplicaProyectos().trim()
                        : VALOR_DEFECTO_CERO);

        entity.setMinimoHoras(
                dto.minimoHoras() != null
                    ? String.valueOf(dto.minimoHoras())
                    : null);
        entity.setMaximoHoras(
                dto.maximoHoras() != null
                    ? String.valueOf(dto.maximoHoras())
                    : null);
        entity.setEstado(normalizeStatus(dto.estado()));
        entity.setRegistradoPor(
                RegistradoPorUtils.value(
                        entity.getId() == null
                                ? Accion.INSERT
                                : Accion.UPDATE
                )
        );
        entity.setFechaCambio(new Date());
    }

    private void validateActivityType(TipoActividadFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La información del tipo de actividad es obligatoria"
            );
        }

        if (!StringUtils.hasText(dto.nombre())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }

        if (dto.minimoHoras() != null && dto.minimoHoras() < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El mínimo de horas no puede ser negativo");
        }

        if (dto.minimoHoras() != null && dto.maximoHoras() != null && dto.maximoHoras() < dto.minimoHoras()) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El máximo de horas debe ser mayor o igual al mínimo de horas"
            );
        }
    }

    private String normalizeStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return ESTADO_ACTIVO;
        }

        String normalized = status.trim().toUpperCase();

        return ESTADO_INACTIVO.equals(normalized)
                || "INACTIVO".equals(normalized)
                || "I".equals(normalized)
                ? ESTADO_INACTIVO
                : ESTADO_ACTIVO;
    }
}
