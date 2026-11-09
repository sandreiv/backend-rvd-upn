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
 * 06/11/2026 - Búsqueda de docente de carrera vigente en trabajador labor.
 * 06/11/2026 - Alta de carga docente.
 * 09/11/2026 - Listado de docentes por tipo de vinculación de la convocatoria.
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.BusquedaDocenteMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.DocenteCargaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.CoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TipoVinculacionMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.BusquedaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosPrecargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.EscalafonEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.EstadosEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosCategoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosVigenciaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoVinculacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.EscalafonRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.EstadosRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosCategoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosVigenciaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoVinculacionConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoVinculacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionPersonaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocentePreasignacionProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.CargaService;
import co.edu.unipamplona.ciadti.rvd.util.FechasConvocatoriaCalculator;
import co.edu.unipamplona.ciadti.rvd.util.NormalizarParametros;
import co.edu.unipamplona.ciadti.rvd.util.ParseUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
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
    private static final String ORDEN_ESTADO_INICIAL = "1";


    private final CoordinacionRepository coordinacionRepository;
    private final ConvocatoriaRepository convocatoriaRepository;
    private final TipoVinculacionConvocatoriaRepository tipoVinculacionConvocatoriaRepository;
    private final TipoVinculacionRepository tipoVinculacionRepository;
    private final CoordinacionMapper coordinacionMapper;
    private final TipoVinculacionMapper tipoVinculacionMapper;
    private final BusquedaDocenteMapper busquedaDocenteMapper;
    private final DocenteCargaMapper docenteCargaMapper;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final PuntosVigenciaRepository puntosVigenciaRepository;
    private final PuntosCategoriaRepository puntosCategoriaRepository;
    private final EscalafonRepository escalafonRepository;
    private final CargaDocenteRepository cargaDocenteRepository;
    private final EstadosRepository estadosRepository;

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
        return searchProfessors(nombre, documento, anio, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusquedaDocenteDTO> searchCareerProfessor(String nombre, String documento, Long anio) {
        return searchProfessors(nombre, documento, anio, true);
    }

    private List<BusquedaDocenteDTO> searchProfessors(
            String nombre,
            String documento,
            Long anio,
            boolean career) {
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
        List<DocentePreasignacionProjection> professors =
                career
                        ? personaGeneralRepository.searchCareerProfessorsForPreassignment(nombreParam, documentoParam)
                        : personaGeneralRepository.searchProfessorsForPreassignment(nombreParam, documentoParam);

        return busquedaDocenteMapper.toDtoList(professors)
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

    @Override
    @Transactional(readOnly = true)
    public List<DocenteCargaDTO> listProfessors(Long idTipoVinculacionConvocatoria) {
        log.debug("listProfessors ===> Listando docentes. idTipoVinculacionConvocatoria={}",
                idTipoVinculacionConvocatoria);
        if (idTipoVinculacionConvocatoria == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de vinculación de la convocatoria es obligatorio.");
        }
        if (!tipoVinculacionConvocatoriaRepository.existsById(idTipoVinculacionConvocatoria)) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe el tipo de vinculación de la convocatoria.");
        }
        List<DocenteCargaDTO> docentes = docenteCargaMapper.toDtoList(
                cargaDocenteRepository.findProfessorsByTipoVinculacionConvocatoria(
                        idTipoVinculacionConvocatoria));
        log.info("listProfessors ===> Docentes listados. idTipoVinculacionConvocatoria={}, total={}",
                idTipoVinculacionConvocatoria, docentes.size());
        return docentes;
    }

    @Override
    @Transactional
    public void addProfessor(CargaDocenteFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga docente es obligatoria.");
        }
        if (dto.idTipoVinculacionConvocatoria() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de vinculación de la convocatoria es obligatorio.");
        }
        if (!tipoVinculacionConvocatoriaRepository.existsById(dto.idTipoVinculacionConvocatoria())) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe el tipo de vinculación de la convocatoria.");
        }
        if (dto.idCoordinacion() != null && !coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinación.");
        }
        if (dto.idPersonaGeneral() != null
                && dto.idCoordinacion() != null
                && cargaDocenteRepository.existsByIdPersonaGeneralAndIdTipoVinculacionConvocatoriaAndIdCoordinacion(
                        dto.idPersonaGeneral(),
                        dto.idTipoVinculacionConvocatoria(),
                        dto.idCoordinacion())) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El docente ya se encuentra registrado en esta vinculación y coordinación.");
        }

        CargaDocenteEntity entity = new CargaDocenteEntity();
        entity.setIdPersonaGeneral(dto.idPersonaGeneral());
        entity.setIdTipoVinculacionConvocatoria(dto.idTipoVinculacionConvocatoria());
        entity.setIdCategoriaCatedratico(dto.idCategoriaCatedratico());
        entity.setIdCoordinacion(dto.idCoordinacion());
        entity.setFechaInicio(toDate(dto.fechaInicio()));
        entity.setFechaFin(toDate(dto.fechaFin()));
        entity.setValorContrato(dto.valorContrato());
        entity.setValorPrestaciones(dto.valorPrestaciones());
        entity.setSalario(dto.asignacionSalarial());
        entity.setTotalContrato(dto.totalContrato());
        entity.setHoras(dto.horas());
        entity.setValorHora(dto.valorHora());
        entity.setPuntos(dto.puntos());
        entity.setValorPunto(dto.valorPunto());
        entity.setSemanas(dto.semanas());
        entity.setEsNovedad(dto.esNovedad());
        entity.setIdNovedad(dto.idNovedad());
        entity.setObservacion(dto.observacion());
        entity.setIdEstado(estadoInicialId());
        entity.setEstado("0");
        entity.setVigente("1");
        entity.setOnceMeses(FechasConvocatoriaCalculator.calcularOnceMesesPorSemanas(dto.semanas()));
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        entity.setFechaCambio(new Date());
        
        cargaDocenteRepository.save(entity);
    }

    private Long estadoInicialId() {
        EstadosEntity estado = estadosRepository.findFirstByOrden(ORDEN_ESTADO_INICIAL)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el estado con orden 1."));
        return estado.getId();
    }

    private static Date toDate(LocalDate fecha) {
        if (fecha == null) {
            return null;
        }
        return java.sql.Date.valueOf(fecha);
    }


}
