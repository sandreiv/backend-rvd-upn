/**
 * Aplicación: rvd
 * Archivo: NovedadAdministracionServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 24/11/2026
 * Modificaciones:
 * 24/11/2026 - Creación inicial para administración de tablas básicas (NOVEDADES)
 * 24/11/2026 - Ajuste a RVDIES.NOVEDADES: solo tipo y descripción; el borrado
 *              es directo porque el esquema no tiene procedimiento PR_RVD_D_*
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.NovedadMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.NovedadAdministracionService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NovedadAdministracionServiceImpl
        implements NovedadAdministracionService {

    /** Longitudes reales de RVDIES.NOVEDADES.NOVE_TIPO y NOVE_DESCRIPCION. */
    private static final int MAX_TIPO = 30;
    private static final int MAX_DESCRIPCION = 30;

    private final NovedadRepository novedadRepository;

    private final NovedadMapper novedadMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NovedadListadoDTO> listNovedades() {

        List<NovedadListadoDTO> result =
                novedadMapper.toListadoDTOList(
                        novedadRepository.findAllNovedades()
                );

        log.info(
                "listNovedades ===> Novedades listadas. total={}",
                result.size()
        );

        return result;
    }

    @Override
    @Transactional
    public void saveNovedad(
            NovedadFormularioDTO dto
    ) {

        validate(dto);

        String tipo = normalize(dto.tipo());

        log.info(
                "saveNovedad ===> Guardando novedad. tipo={}",
                tipo
        );

        if (novedadRepository.countByTipo(tipo) > 0) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Ya existe una novedad con el tipo indicado"
            );
        }

        NovedadEntity entity = new NovedadEntity();

        fill(entity, dto);

        novedadRepository.save(entity);

        log.info(
                "saveNovedad ===> Novedad guardada. tipo={}",
                tipo
        );
    }

    @Override
    @Transactional
    public void updateNovedad(
            Long id,
            NovedadFormularioDTO dto
    ) {

        validate(dto);

        String tipo = normalize(dto.tipo());

        log.info(
                "updateNovedad ===> Actualizando novedad. id={}, tipo={}",
                id,
                tipo
        );

        NovedadEntity entity =
                novedadRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "No existe la novedad con id " + id
                                )
                        );

        if (
            novedadRepository
                    .countByTipoAndIdNot(tipo, id) > 0
        ) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Ya existe una novedad con el tipo indicado"
            );
        }

        fill(entity, dto);

        novedadRepository.save(entity);

        log.info(
                "updateNovedad ===> Novedad actualizada. id={}",
                id
        );
    }

    @Override
    @Transactional
    public void deleteNovedad(
            Long id
    ) {

        log.info(
                "deleteNovedad ===> Eliminando novedad. id={}",
                id
        );

        NovedadEntity entity =
                novedadRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "No existe la novedad con id " + id
                                )
                        );

        long usos = novedadRepository.countUsosNovedad(id);

        if (usos > 0) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "La novedad está asociada a " + usos
                            + " registro(s) de carga docente y no se puede eliminar"
            );
        }

        novedadRepository.delete(entity);
        novedadRepository.flush();

        log.info(
                "deleteNovedad ===> Novedad eliminada. id={}",
                id
        );
    }

    @Override
    @Transactional
    public void deleteBulkNovedades(
            List<Long> ids
    ) {

        if (
            ids == null ||
            ids.isEmpty()
        ) {
            return;
        }

        log.info(
                "deleteBulkNovedades ===> Eliminación masiva. total={}",
                ids.size()
        );

        for (Long id : ids) {
            deleteNovedad(id);
        }
    }

    private void fill(
            NovedadEntity entity,
            NovedadFormularioDTO dto
    ) {

        entity.setTipo(
                normalize(dto.tipo())
        );

        entity.setDescripcion(
                normalize(dto.descripcion())
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
            NovedadFormularioDTO dto
    ) {

        if (dto == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La información de la novedad es obligatoria"
            );
        }

        validateText(
                dto.tipo(),
                "El tipo de novedad es obligatorio",
                MAX_TIPO,
                "El tipo de novedad no puede superar " + MAX_TIPO + " caracteres"
        );

        validateText(
                dto.descripcion(),
                "La descripción es obligatoria",
                MAX_DESCRIPCION,
                "La descripción no puede superar " + MAX_DESCRIPCION + " caracteres"
        );
    }

    private void validateText(
            String value,
            String requiredMessage,
            int maxLength,
            String lengthMessage
    ) {

        if (!StringUtils.hasText(value)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    requiredMessage
            );
        }

        if (value.trim().length() > maxLength) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    lengthMessage
            );
        }
    }

    private String normalize(
            String value
    ) {
        return value == null ? null : value.trim();
    }
}
