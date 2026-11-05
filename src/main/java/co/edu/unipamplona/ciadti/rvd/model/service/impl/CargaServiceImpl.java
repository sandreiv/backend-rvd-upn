/**
 * Aplicación: rvd
 * Archivo: CargaServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 05/11/2026 - Forma de pago desde RVDIES.TIPOVINCULACION.TIVI_FORMAPAGO.
 * 05/11/2026 - Búsqueda de docente con valor del punto y asignación salarial.
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.BusquedaDocenteMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.CoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TipoVinculacionMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.BusquedaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosPrecargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.EscalafonEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosCategoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosVigenciaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoVinculacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.EscalafonRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosCategoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosVigenciaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoVinculacionConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoVinculacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionPersonaProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.CargaService;
import co.edu.unipamplona.ciadti.rvd.util.NormalizarParametros;
import co.edu.unipamplona.ciadti.rvd.util.ParseUtils;
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
    private final TipoVinculacionRepository tipoVinculacionRepository;
    private final CoordinacionMapper coordinacionMapper;
    private final TipoVinculacionMapper tipoVinculacionMapper;
    private final BusquedaDocenteMapper busquedaDocenteMapper;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final PuntosVigenciaRepository puntosVigenciaRepository;
    private final PuntosCategoriaRepository puntosCategoriaRepository;
    private final EscalafonRepository escalafonRepository;



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
        
        return tipoVinculacionMapper.toListadoDto(
                tipoVinculacionConvocatoriaRepository.findByIdConvocatoria(idConvocatoria));
    }


    @Override
    @Transactional(readOnly = true)
    public List<BusquedaDocenteDTO> searchProfessor(String nombre, String documento, Long anio) {
        String nombreParam = NormalizarParametros.normalizeParam(nombre);
        String documentoParam = NormalizarParametros.normalizeParam(documento);

        if (nombreParam == null && documentoParam == null) {
            return Collections.emptyList();
        }
        if (anio == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El año de la vigencia es obligatorio");
        }

        BigDecimal valorPunto = valorPuntoVigencia(anio);
        BigDecimal valorPuntoEscalado = valorPunto.setScale(2, RoundingMode.HALF_UP);

        return busquedaDocenteMapper.toDtoList(
                personaGeneralRepository.searchProfessorsForPreassignment(nombreParam, documentoParam))
                .stream()
                .map(docente -> withSalaryAssignment(docente, valorPunto, valorPuntoEscalado))
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public ValorPuntosCargaDTO getValuePointsPreload(
            Long anio,
            Long idCategoriaCatedratico,
            Long idPersonaGeneral,
            Long idModalidadContratacion,
            Long idTipoVinculacion) {
        BigDecimal valorPunto = valorPuntoVigencia(anio);

        PuntosCategoriaEntity puntosCategoria = puntosCategoriaRepository.findByIdCategoriaCatedratico(idCategoriaCatedratico);
        BigDecimal puntosCategoriaValor = ParseUtils.parseBigDecimal(puntosCategoria.getPuntos(), "puntos de la categoria");
        BigDecimal valorHora = valorPunto.multiply(puntosCategoriaValor).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorPuntoEscalado = valorPunto.setScale(2, RoundingMode.HALF_UP);
        String formaPago = resolveFormaPago(idTipoVinculacion);

        if (idPersonaGeneral == null) {
            return new ValorPuntosCargaDTO(valorHora, valorPuntoEscalado, null, null, formaPago);
        }

        EscalafonEntity escalafon = escalafonRepository.findByIdCategoriaCatedratico(idCategoriaCatedratico, idPersonaGeneral);
        if (escalafon == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe escalafon para la persona.");
        }

        BigDecimal puntosDocente = ParseUtils.parseBigDecimal(escalafon.getPuntos(), "puntos del docente (escalafon)");
        BigDecimal asignacionSalarial = asignacionSalarial(puntosDocente, valorPunto);

        return new ValorPuntosCargaDTO(
                valorHora,
                valorPuntoEscalado,
                puntosDocente.setScale(2, RoundingMode.HALF_UP),
                asignacionSalarial,
                formaPago);
    }

    private BigDecimal valorPuntoVigencia(Long anio) {
        PuntosVigenciaEntity vigencia = puntosVigenciaRepository.findByAnio(anio)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe valor de puntos para la vigencia " + anio));
        return ParseUtils.parseBigDecimal(vigencia.getValorPunto(), "valor del punto de la vigencia");
    }

    private static BigDecimal asignacionSalarial(BigDecimal puntosDocente, BigDecimal valorPunto) {
        return puntosDocente.multiply(valorPunto).setScale(2, RoundingMode.HALF_UP);
    }

    private BusquedaDocenteDTO withSalaryAssignment(
            BusquedaDocenteDTO docente,
            BigDecimal valorPunto,
            BigDecimal valorPuntoEscalado) {
        BigDecimal asignacion = null;
        if (docente.escalafon() != null) {
            BigDecimal puntosDocente = ParseUtils.parseBigDecimal(
                    docente.escalafon().puntos(),
                    "puntos del docente (escalafon)");
            asignacion = asignacionSalarial(puntosDocente, valorPunto);
        }
        return new BusquedaDocenteDTO(
                docente.id(),
                docente.documentoIdentidad(),
                docente.nombreCompleto(),
                docente.categoriaCatedratico(),
                docente.escalafon(),
                docente.cargaDocente(),
                valorPuntoEscalado,
                asignacion);
    }

    private String resolveFormaPago(Long idTipoVinculacion) {
        if (idTipoVinculacion == null) {
            return null;
        }
        TipoVinculacionEntity tipoVinculacion = tipoVinculacionRepository.findById(idTipoVinculacion)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el tipo de vinculación."));
        return tipoVinculacion.getFormaPago();
    }


}
