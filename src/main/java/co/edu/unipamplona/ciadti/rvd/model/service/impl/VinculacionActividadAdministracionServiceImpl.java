/**
 * Aplicación: rvd
 * Archivo: VinculacionActividadAdministracionServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Juan Diego Florez Vera - Creación inicial para administración de tablas básicas
 * 25/11/2026 - Borrado directo: RVDIES no tiene procedimiento PR_RVD_D_* para
 *              VINCULACIONACTIVIDADES, y ninguna tabla referencia VIAC_ID
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.VinculacionActividadMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.OpcionAgrupadaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.VinculacionActividadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.VinculacionActividadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.VinculacionActividadesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.VinculacionActividadRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.VinculacionActividadAdministracionService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VinculacionActividadAdministracionServiceImpl
        implements VinculacionActividadAdministracionService {

    /** VIAC_MINIMOHORAS y VIAC_MAXIMOHORAS son VARCHAR2(30) pero guardan números. */
    private static final Pattern HORAS = Pattern.compile("^\\d{1,3}([.,]\\d{1,2})?$");
    private static final int MAX_HORAS_LENGTH = 30;

    private final VinculacionActividadRepository vinculacionActividadRepository;

    private final VinculacionActividadMapper vinculacionActividadMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VinculacionActividadListadoDTO> listVinculacionActividades() {

        List<VinculacionActividadListadoDTO> result =
                vinculacionActividadMapper.toListadoDTOList(
                        vinculacionActividadRepository
                                .findAllVinculacionActividades()
                );

        log.info(
                "listVinculacionActividades ===> Registros listados. total={}",
                result.size()
        );

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpcionAgrupadaDTO> listTiposVinculacion() {

        List<OpcionAgrupadaDTO> result =
                vinculacionActividadMapper.toOpcionDTOList(
                        vinculacionActividadRepository
                                .findTiposVinculacion()
                );

        log.info(
                "listTiposVinculacion ===> Tipos de vinculación. total={}",
                result.size()
        );

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpcionAgrupadaDTO> listTiposActividad() {

        List<OpcionAgrupadaDTO> result =
                vinculacionActividadMapper.toOpcionDTOList(
                        vinculacionActividadRepository
                                .findTiposActividadSeleccionables()
                );

        log.info(
                "listTiposActividad ===> Actividades seleccionables. total={}",
                result.size()
        );

        return result;
    }

    @Override
    @Transactional
    public void saveVinculacionActividad(
            VinculacionActividadFormularioDTO dto
    ) {

        validate(dto);

        log.info(
                "saveVinculacionActividad ===> Guardando. tiviId={}, tiacId={}",
                dto.idTipoVinculacion(),
                dto.idTipoActividad()
        );

        if (
            vinculacionActividadRepository
                    .countByVinculacionAndActividad(
                            dto.idTipoVinculacion(),
                            dto.idTipoActividad()
                    ) > 0
        ) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Ya existe la relación entre ese tipo de vinculación y esa actividad"
            );
        }

        VinculacionActividadesEntity entity =
                new VinculacionActividadesEntity();

        fill(entity, dto);

        vinculacionActividadRepository.save(entity);

        log.info(
                "saveVinculacionActividad ===> Registro guardado. tiviId={}, tiacId={}",
                dto.idTipoVinculacion(),
                dto.idTipoActividad()
        );
    }

    @Override
    @Transactional
    public void updateVinculacionActividad(
            Long id,
            VinculacionActividadFormularioDTO dto
    ) {

        validate(dto);

        log.info(
                "updateVinculacionActividad ===> Actualizando. id={}, tiviId={}, tiacId={}",
                id,
                dto.idTipoVinculacion(),
                dto.idTipoActividad()
        );

        VinculacionActividadesEntity entity =
                vinculacionActividadRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "No existe el registro con id " + id
                                )
                        );

        if (
            vinculacionActividadRepository
                    .countByVinculacionAndActividadAndIdNot(
                            dto.idTipoVinculacion(),
                            dto.idTipoActividad(),
                            id
                    ) > 0
        ) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Ya existe la relación entre ese tipo de vinculación y esa actividad"
            );
        }

        fill(entity, dto);

        vinculacionActividadRepository.save(entity);

        log.info(
                "updateVinculacionActividad ===> Registro actualizado. id={}",
                id
        );
    }

    @Override
    @Transactional
    public void deleteVinculacionActividad(
            Long id
    ) {

        log.info(
                "deleteVinculacionActividad ===> Eliminando. id={}",
                id
        );

        VinculacionActividadesEntity entity =
                vinculacionActividadRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "No existe el registro con id " + id
                                )
                        );

        vinculacionActividadRepository.delete(entity);
        vinculacionActividadRepository.flush();

        log.info(
                "deleteVinculacionActividad ===> Registro eliminado. id={}",
                id
        );
    }

    @Override
    @Transactional
    public void deleteBulkVinculacionActividades(
            List<Long> ids
    ) {

        if (
            ids == null ||
            ids.isEmpty()
        ) {
            return;
        }

        log.info(
                "deleteBulkVinculacionActividades ===> Eliminación masiva. total={}",
                ids.size()
        );

        for (Long id : ids) {
            deleteVinculacionActividad(id);
        }
    }

    private void fill(
            VinculacionActividadesEntity entity,
            VinculacionActividadFormularioDTO dto
    ) {

        entity.setIdTipoVinculacion(
                dto.idTipoVinculacion()
        );

        entity.setIdTipoActividad(
                dto.idTipoActividad()
        );

        entity.setEstado(
                normalizeEstado(dto.estado())
        );

        entity.setMinimoHoras(
                normalizeHoras(dto.minimoHoras())
        );

        entity.setMaximoHoras(
                normalizeHoras(dto.maximoHoras())
        );

        entity.setRegistradoPor(
                RegistradoPorUtils.value(
                        entity.getId() == null
                                ? Accion.INSERT
                                : Accion.UPDATE
                )
        );

        entity.setFechaCambio(
                new Date()
        );
    }

    private void validate(
            VinculacionActividadFormularioDTO dto
    ) {

        if (dto == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La información del registro es obligatoria"
            );
        }

        if (dto.idTipoVinculacion() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de vinculación es obligatorio"
            );
        }

        if (
            vinculacionActividadRepository
                    .countTipoVinculacion(dto.idTipoVinculacion()) == 0
        ) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de vinculación seleccionado no existe"
            );
        }

        if (dto.idTipoActividad() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La actividad es obligatoria"
            );
        }

        if (
            vinculacionActividadRepository
                    .countTipoActividad(dto.idTipoActividad()) == 0
        ) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La actividad seleccionada no existe"
            );
        }

        normalizeEstado(dto.estado());
        validateHoras(dto.minimoHoras(), "mínimas");
        validateHoras(dto.maximoHoras(), "máximas");
        validateRango(dto.minimoHoras(), dto.maximoHoras());
    }

    private void validateHoras(
            String value,
            String etiqueta
    ) {

        if (!StringUtils.hasText(value)) {
            return;
        }

        String horas = value.trim();

        if (horas.length() > MAX_HORAS_LENGTH) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Las horas " + etiqueta + " no pueden superar "
                            + MAX_HORAS_LENGTH + " caracteres"
            );
        }

        if (!HORAS.matcher(horas).matches()) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Las horas " + etiqueta
                            + " deben ser numéricas (por ejemplo 2 o 1.5)"
            );
        }
    }

    private void validateRango(
            String minimo,
            String maximo
    ) {

        if (
            !StringUtils.hasText(minimo) ||
            !StringUtils.hasText(maximo)
        ) {
            return;
        }

        BigDecimal valorMinimo = toBigDecimal(minimo);
        BigDecimal valorMaximo = toBigDecimal(maximo);

        if (valorMinimo.compareTo(valorMaximo) > 0) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Las horas mínimas no pueden ser mayores que las máximas"
            );
        }
    }

    private BigDecimal toBigDecimal(
            String value
    ) {
        return new BigDecimal(
                value.trim().replace(',', '.')
        );
    }

    private String normalizeHoras(
            String value
    ) {
        return StringUtils.hasText(value)
                ? value.trim().replace(',', '.')
                : null;
    }

    private String normalizeEstado(
            String value
    ) {

        if (!StringUtils.hasText(value)) {
            return "1";
        }

        String estado = value.trim();

        if (!"1".equals(estado) && !"0".equals(estado)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El estado debe ser '1' (activo) o '0' (inactivo)"
            );
        }

        return estado;
    }
}
