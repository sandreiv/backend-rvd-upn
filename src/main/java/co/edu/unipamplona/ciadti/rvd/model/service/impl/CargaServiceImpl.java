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
 * 09/11/2026 - Árbol de tipos de actividad desde RVDIES.TIPOACTIVIDADES.
 * 09/11/2026 - Restricciones de actividad por tipo de vinculación.
 * 11/11/2026 - Programas por unidad, coordinación y periodo con restricción de director.
 * 11/11/2026 - Pensum por programa y materias del pensum.
 * 11/11/2026 - Grupos de la materia en el periodo de la convocatoria y sus extensiones.
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.BusquedaDocenteMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.DocenteCargaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.GrupoCargaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.MateriaPensumMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.PensumListadoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ProgramaListadoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.RestriccionActividadListadoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.CoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TipoActividadListadoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TipoVinculacionMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.BusquedaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionActividadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PensumListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
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
import co.edu.unipamplona.ciadti.rvd.model.repository.GrupoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.MateriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PensumRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PeriodoUniversidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProgramaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosCategoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosVigenciaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoVinculacionConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoVinculacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionPersonaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadArbolProjection;
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
    private final TipoActividadesRepository tipoActividadesRepository;
    private final TipoActividadListadoMapper tipoActividadListadoMapper;
    private final RestriccionActividadesRepository restriccionActividadesRepository;
    private final RestriccionActividadListadoMapper restriccionActividadListadoMapper;
    private final ProgramaRepository programaRepository;
    private final ProgramaListadoMapper programaListadoMapper;
    private final PeriodoUniversidadRepository periodoUniversidadRepository;
    private final PersonaCoordinacionRepository personaCoordinacionRepository;
    private final PensumRepository pensumRepository;
    private final PensumListadoMapper pensumListadoMapper;
    private final MateriaRepository materiaRepository;
    private final MateriaPensumMapper materiaPensumMapper;
    private final GrupoRepository grupoRepository;
    private final GrupoCargaMapper grupoCargaMapper;

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
    public List<TipoActividadListadoDTO> listActivityTypes() {
        log.info("listActivityTypes ===> Listando tipos de actividad en árbol");
        List<TipoActividadArbolProjection> rows = tipoActividadesRepository.findAllForActivityTree();
        if (rows == null || rows.isEmpty()) {
            return List.of();
        }
        Map<Long, List<TipoActividadArbolProjection>> porPadre = new LinkedHashMap<>();
        for (TipoActividadArbolProjection row : rows) {
            porPadre.computeIfAbsent(row.getIdPadre(), key -> new ArrayList<>()).add(row);
        }
        return buildActivityTree(null, porPadre, new HashSet<>());
    }

    private List<TipoActividadListadoDTO> buildActivityTree(
            Long idPadre,
            Map<Long, List<TipoActividadArbolProjection>> porPadre,
            Set<Long> visitados) {
        List<TipoActividadArbolProjection> nodos = porPadre.getOrDefault(idPadre, List.of());
        if (nodos.isEmpty()) {
            return List.of();
        }
        List<TipoActividadListadoDTO> arbol = new ArrayList<>();
        for (TipoActividadArbolProjection nodo : nodos) {
            if (nodo.getId() == null || !visitados.add(nodo.getId())) {
                continue;
            }
            arbol.add(tipoActividadListadoMapper.toDto(
                    nodo,
                    buildActivityTree(nodo.getId(), porPadre, visitados)));
        }
        return List.copyOf(arbol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestriccionActividadListadoDTO> listActivityRestrictions(Long idTipoVinculacion) {
        if (idTipoVinculacion == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de vinculación es obligatorio.");
        }
        if (!tipoVinculacionRepository.existsById(idTipoVinculacion)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el tipo de vinculación.");
        }
        log.info("listActivityRestrictions ===> Listando restricciones de actividad. idTipoVinculacion={}",
                idTipoVinculacion);
        return restriccionActividadListadoMapper.toDtoList(
                restriccionActividadesRepository.findByIdTipoVinculacion(idTipoVinculacion));
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

    @Override
    @Transactional(readOnly = true)
    public List<ProgramaDTO> listPrograms(
            Long idCoordinacion,
            Long idUnidadRegional,
            Long idPeriodoUniversidad) {
        if (idCoordinacion == null || idUnidadRegional == null || idPeriodoUniversidad == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La coordinación, la unidad regional y el periodo universitario son obligatorios.");
        }
        Long idPersona;
        try {
            idPersona = SecurityUtils.requireIdPersona();
        } catch (IllegalStateException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        if (!coordinacionRepository.existsById(idCoordinacion)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinación.");
        }
        if (!coordinacionRepository.existsByIdAndIdUnidadRegional(idCoordinacion, idUnidadRegional)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La coordinación no pertenece a la unidad regional indicada.");
        }
        if (!periodoUniversidadRepository.existsById(idPeriodoUniversidad)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el periodo universitario.");
        }
        if (!personaCoordinacionRepository.existsActiveByCoordinacionAndPersona(idCoordinacion, idPersona)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "La persona no está activa en la coordinación indicada.");
        }
        return programaListadoMapper.toDtoList(
                programaRepository.findByCoordinacionAndUnidadRegional(
                        idCoordinacion,
                        idUnidadRegional,
                        idPersona));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PensumListadoDTO> listPensums(Long idPrograma) {
        if (idPrograma == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El programa es obligatorio.");
        }
        if (!programaRepository.existsById(idPrograma)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el programa.");
        }
        
        return pensumListadoMapper.toDtoList(pensumRepository.findByIdPrograma(idPrograma));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaDTO> listSubjects(Long idPensum) {
        if (idPensum == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El pensum es obligatorio.");
        }
        if (!pensumRepository.existsById(idPensum)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el pensum.");
        }
        
        return materiaPensumMapper.toDtoList(materiaRepository.findByIdPensum(idPensum));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoDTO> listSubjectGroups(Long idConvocatoria, String codigoMateria) {
        if (idConvocatoria == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La convocatoria es obligatoria.");
        }
        if (codigoMateria == null || codigoMateria.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El código de la materia es obligatorio.");
        }
        if (!convocatoriaRepository.existsById(idConvocatoria)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la convocatoria.");
        }
        return grupoCargaMapper.toDtoList(
                grupoRepository.findByMateriaAndConvocatoria(codigoMateria.trim(), idConvocatoria));
    }



}
