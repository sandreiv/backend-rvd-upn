/**
 * Aplicación: rvd
 * Archivo: TiposDocumentoContratacionServiceImpl.java
 * Paquete: Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/11/2026
 * Modificaciones:
 * 27/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.TiposDocumentoContratacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TiposDocumentoContratacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoDocumentoContratacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoDocumentoContratacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.TiposDocumentoContratacionService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TiposDocumentoContratacionServiceImpl implements TiposDocumentoContratacionService {
    
    private final TipoDocumentoContratacionRepository tipoDocumentoContratacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TiposDocumentoContratacionListadoDTO> listDocumentTypes() {
        log.info("listDocumentTypes ===> Listando tipos de documentos para la contratación");

        List<TiposDocumentoContratacionListadoDTO> result = tipoDocumentoContratacionRepository.findAllDocumentTypes()
            .stream()
            .map(tipoDoc -> new TiposDocumentoContratacionListadoDTO(
                tipoDoc.getId(),
                tipoDoc.getNombre(),
                tipoDoc.getDescripcion(),
                tipoDoc.getObligatorio()
            ))
            .toList();
        
        log.info("listDocumentTypes ===> Tipos de documentos para la contratación listados. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveDocumentType(TiposDocumentoContratacionFormularioDTO dto) {
        log.info("saveDocumentType ===> Guardando tipo de documento para la contratación. nombre={}",
            dto != null ? dto.nombre() : null
        );

        validate(dto);

        if (tipoDocumentoContratacionRepository.existsByNombre(dto.nombre())) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un tipo de documento registrado con el nombre " + dto.nombre());
        }

        TipoDocumentoContratacionEntity entity = new TipoDocumentoContratacionEntity();
        fill(entity, dto);

        tipoDocumentoContratacionRepository.save(entity);

        log.info("saveDocumentType ===> Tipo de documento para la contratación guardado. nombre={}", dto.nombre());
    }

    @Override
    @Transactional
    public void updateDocumentType(Long id, TiposDocumentoContratacionFormularioDTO dto) {
        log.info("updateDocumentType ===> Actualizando tipo de documento para la contratación. id={}, nombre={}",
            id,
            dto != null ? dto.nombre() : null
        );

        validate(dto);

        TipoDocumentoContratacionEntity entity = tipoDocumentoContratacionRepository.findById(id)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe el tipo de documento con id " + id));

        fill(entity, dto);

        tipoDocumentoContratacionRepository.save(entity);
        
        log.info("updateDocumentType ===> Tipo de documento para la contratación actualizado. id={}", id);
    }

    @Override
    @Transactional
    public void deleteDocumentType(Long id) {
        log.info("deleteDocumentType ===> Eliminando tipo de documento para la contratación. id={}", id);

        if (id == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El identificador del tipo de documento es obligatorio");
        }

        TipoDocumentoContratacionEntity entity = tipoDocumentoContratacionRepository.findById(id)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe el tipo de documento con id " + id));

        tipoDocumentoContratacionRepository.delete(entity);

        log.info("deleteDocumentType ===> Tipo de documento para la contratación eliminado. id={}", id);
    }

    @Override
    @Transactional
    public void deleteBulkDocumentTypes(List<Long> ids) {
        log.info("deleteBulkDocumentTypes ===> Eliminando masivamente tipos de documentos para la contratación. total={}",
            ids != null ? ids.size() : 0);

        if (ids == null || ids.isEmpty()) {
            log.debug("deleteBulkDocumentTypes ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (Long id : ids) {
            deleteDocumentType(id);
        }

        log.info("deleteBulkDocumentTypes ===> Eliminación masiva de tipos de documentos para la contratación finalizada. total={}", ids.size());
    }


    private void fill(TipoDocumentoContratacionEntity entity, TiposDocumentoContratacionFormularioDTO dto) {
        entity.setNombre(normalizeText(dto.nombre()));
        entity.setDescripcion(normalizeText(dto.descripcion()));
        entity.setObligatorio(normalizeStatus(dto.esObligatorio()));

        entity.setRegistradoPor(
            RegistradoPorUtils.value(
                entity.getId() == null
                    ? Accion.INSERT
                    : Accion.UPDATE
            )
        );
        entity.setFechaCambio(new Date());
    }

    private void validate(TiposDocumentoContratacionFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información del tipo de documento es obligatoria");
        }

        String nombre = normalizeText(dto.nombre());
        String descripcion = normalizeText(dto.descripcion());

        if (!StringUtils.hasText(nombre)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El nombre del tipo de documento es obligatorio");
        }
        if (!StringUtils.hasText(descripcion)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La descripción del tipo de documento es obligatoria");
        }
    }

    private String normalizeText(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String normalizeStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return "1";
        }

        String normalized = status.trim().toUpperCase();

        return "0".equals(normalized) || "INACTIVO".equals(normalized) || "I".equals(normalized) ? "0" : "1";
    }
}
