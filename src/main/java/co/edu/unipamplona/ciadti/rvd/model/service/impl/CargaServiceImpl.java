/**
 * Aplicación: rvd
 * Archivo: CargaServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.CoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TipoVinculacionMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoVinculacionConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionPersonaProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.CargaService;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CargaServiceImpl implements CargaService {

    private static final String ROL_COORDINADOR = "Coordinador";
    private static final String ROL_DECANO = "Decano";
    private static final String ROL_DESARROLLO = "Desarrollo academico";
    private static final String ROL_VICERRECTORIA = "Vicerrectoria academica";
    private static final String ROL_TALENTO = "Talento Humano";


    private final CoordinacionRepository coordinacionRepository;

    private final ConvocatoriaRepository convocatoriaRepository;

    private final TipoVinculacionConvocatoriaRepository tipoVinculacionConvocatoriaRepository;

    private final CoordinacionMapper coordinacionMapper;

    private final TipoVinculacionMapper tipoVinculacionMapper;


    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionDTO> findCoordinationsByPerson(Long idConvocatoria, Long idPeriodoUniversidad) {
        if (idPeriodoUniversidad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio");
        }
        Long idPersona;
        try {
            idPersona = SecurityUtils.requireIdPersona();
        } catch (IllegalStateException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        if (idConvocatoria != null
                && !convocatoriaRepository.existsByIdAndIdPeriodoUniversidad(idConvocatoria, idPeriodoUniversidad)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La convocatoria no pertenece al periodo indicado");
        }
    
        return coordinacionMapper.toDtoList(
                coordinacionRepository.findByConvocatoriaWithCarga(
                        idPeriodoUniversidad, idConvocatoria, idPersona));
    }

    @Override
    @Transactional(readOnly = true)
    public TipoVinculacionListadoDTO findLinkageTypesByCall(Long idConvocatoria) {
        if (idConvocatoria == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La convocatoria es obligatoria");
        }
        if (!convocatoriaRepository.existsById(idConvocatoria)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La convocatoria no existe");
        }
        log.info("findLinkageTypesByCall ===> Listando tipos de vinculación. idConvocatoria={}", idConvocatoria);
        return tipoVinculacionMapper.toListadoDto(
                tipoVinculacionConvocatoriaRepository.findByIdConvocatoria(idConvocatoria));
    }



    private AuthUserDetails requireListadoUser() {
        AuthUserDetails user;
        try {
            user = SecurityUtils.requireUser();
            System.out.println("[DEBUG] USER: " + user);
        } catch (IllegalStateException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        if (user.getIdPersonaGeneral() == null) {
            throw new ApiException(HttpStatus.FORBIDDEN, "El token no trae idPersona");
        }
        if (!hasRole(user, ROL_COORDINADOR) && !hasRole(user, ROL_DECANO) && !hasRole(user, ROL_DESARROLLO) && !hasRole(user, ROL_VICERRECTORIA) && !hasRole(user, ROL_TALENTO)) {
            throw new ApiException(HttpStatus.FORBIDDEN,"El listado de coordinaciones requiere rol Coordinador, Decano, Desarrollo academico, Vicerrectoria academica o Talento Humano");
        }
        return user;
    }

    private void validateListadoFiltros(Long idConvocatoria, Long idPeriodoUniversidad) {
        if (idConvocatoria != null && idPeriodoUniversidad != null) {
            validateConvocatoriaBelongsToPeriod(idConvocatoria, idPeriodoUniversidad);
        }
        if (idConvocatoria == null && idPeriodoUniversidad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio cuando no se envía convocatoria");
        }
    }

    private List<CoordinacionPersonaProjection> listForCoordinador(
            Long idConvocatoria,
            Long idPeriodoUniversidad,
            Long idPersona) {
        return coordinacionRepository.findByConvocatoriaWithCarga(
                idPeriodoUniversidad, idConvocatoria, idPersona);
    }


    private static boolean hasRole(AuthUserDetails user, String role) {
        return user.getRoles().stream()
                .filter(Objects::nonNull)
                .anyMatch(item -> role.equalsIgnoreCase(item.trim()));
    }

    private void validateConvocatoriaBelongsToPeriod(
            Long idConvocatoria,
            Long idPeriodoUniversidad) {
        var periodo = convocatoriaRepository.findPeriodoEntityByConvocatoriaId(idConvocatoria);
        if (periodo == null || !Objects.equals(periodo.getId(), idPeriodoUniversidad)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La convocatoria no pertenece al periodo universitario indicado");
        }
    }


}
