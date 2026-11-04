package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.ConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoExtensionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoExtensionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoVinculacionConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PeriodoExtensionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PeriodoUniversidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoVinculacionConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConvocatoriaPrecargaServiceImpl implements ConvocatoriaPrecargaService {

    private final ConvocatoriaRepository convocatoriaRepository;
    private final ConvocatoriaMapper convocatoriaMapper;
    private final PeriodoUniversidadRepository periodoUniversidadRepository;
    private final TipoVinculacionConvocatoriaRepository tipoVinculacionConvocatoriaRepository;
    private final PeriodoExtensionRepository periodoExtensionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ConvocatoriaDTO> findCallListWithDates(Long idPeriodoUniversidad) {
        if (idPeriodoUniversidad != null) {
            return convocatoriaRepository.findByIdPeriodoUniversidad(idPeriodoUniversidad).stream()
                    .map(convocatoriaMapper::toListDto)
                    .collect(Collectors.toList());
        }
        return convocatoriaRepository.findAllByOrderByIdDesc().stream()
                    .map(convocatoriaMapper::toListDto)
                    .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConvocatoriaDTO> findCallListByFirstPeriodByYear(Long year) {
        return convocatoriaRepository.findByPeriodoYear(year).stream()
                .map(convocatoriaMapper::toListDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(ConvocatoriaFormularioDTO dto) {
        if (dto.id() != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La convocatoria no debe incluir id al crear");
        }

        ConvocatoriaEntity convocatoria = new ConvocatoriaEntity();
        convocatoria.setNombre(dto.nombre() != null ? dto.nombre().trim() : null);
        convocatoria.setDescripcion(dto.descripcion() != null ? dto.descripcion().trim() : null);
        convocatoria.setIdPeriodoUniversidad(dto.idPeriodoUniversidad());
        convocatoria.setIdRelacion(dto.idRelacion());
        convocatoria.setFechaInicio(dto.fechaInicio());
        convocatoria.setFechaFin(dto.fechaFin());
        convocatoria.setEsIntersemestral(dto.esIntersemestral() != null ? dto.esIntersemestral() : "0");
        convocatoria.setEstado("1");
        convocatoria.setFechaCambio(new Date());
        convocatoria.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));

        Long convId = convocatoriaRepository.save(convocatoria).getId();

        guardarVinculaciones(convId, dto.vinculaciones());
        guardarPeriodosExtension(convId, dto.periodosExtension());
    }

    @Override
    @Transactional
    public void update(Long id, ConvocatoriaFormularioDTO dto) {
        ConvocatoriaEntity convocatoria = convocatoriaRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Convocatoria no encontrada"));

        convocatoria.setNombre(dto.nombre() != null ? dto.nombre().trim() : null);
        convocatoria.setDescripcion(dto.descripcion() != null ? dto.descripcion().trim() : null);
        convocatoria.setIdPeriodoUniversidad(dto.idPeriodoUniversidad());
        convocatoria.setIdRelacion(dto.idRelacion());
        convocatoria.setFechaInicio(dto.fechaInicio());
        convocatoria.setFechaFin(dto.fechaFin());
        convocatoria.setEsIntersemestral(dto.esIntersemestral() != null ? dto.esIntersemestral() : "0");
        if (dto.estado() != null) {
            convocatoria.setEstado(dto.estado());
        }
        convocatoria.setFechaCambio(new Date());
        convocatoria.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));

        convocatoriaRepository.save(convocatoria);

        tipoVinculacionConvocatoriaRepository.deleteByIdConvocatoria(id);
        guardarVinculaciones(id, dto.vinculaciones());

        periodoExtensionRepository.deleteByIdConvocatoria(id);
        guardarPeriodosExtension(id, dto.periodosExtension());
    }

    private void guardarVinculaciones(Long convId, List<TipoVinculacionConvocatoriaDTO> vinculaciones) {
        if (vinculaciones == null || vinculaciones.isEmpty()) {
            return;
        }
        for (TipoVinculacionConvocatoriaDTO v : vinculaciones) {
            TipoVinculacionConvocatoriaEntity entity = new TipoVinculacionConvocatoriaEntity();
            entity.setIdConvocatoria(convId);
            entity.setIdTipoVinculacion(v.idTipoVinculacion());
            entity.setEstado(v.estado() != null ? v.estado() : "ACTIVO");
            entity.setFechaCambio(new Date());
            entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
            tipoVinculacionConvocatoriaRepository.save(entity);
        }
    }

    private void guardarPeriodosExtension(Long convId, List<PeriodoExtensionDTO> periodosExtension) {
        if (periodosExtension == null || periodosExtension.isEmpty()) {
            return;
        }
        for (PeriodoExtensionDTO pe : periodosExtension) {
            if (pe.idPeriodoUniversidad() == null) continue;
            PeriodoExtensionEntity entity = new PeriodoExtensionEntity();
            entity.setIdConvocatoria(convId);
            entity.setIdPeriodoUniversidad(pe.idPeriodoUniversidad());
            entity.setFechaCambio(new Date());
            entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
            periodoExtensionRepository.save(entity);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ConvocatoriaFormularioDTO findCallDetail(Long id) {
        ConvocatoriaEntity convocatoria = convocatoriaRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Convocatoria no encontrada"));

        List<TipoVinculacionConvocatoriaDTO> vinculaciones = tipoVinculacionConvocatoriaRepository
                .findByIdConvocatoriaWithVinculacion(id).stream()
                .map(v -> new TipoVinculacionConvocatoriaDTO(
                        v.getId(),
                        v.getIdTipoVinculacion(),
                        null,
                        v.getEstado()
                ))
                .toList();

        List<PeriodoExtensionDTO> periodosExtension = periodoExtensionRepository
                .findByIdConvocatoriaWithPeriodo(id).stream()
                .map(p -> new PeriodoExtensionDTO(
                        p.getId(),
                        p.getIdPeriodoUniversidad(),
                        p.getPeriodoUniversidad() != null
                                ? ((p.getPeriodoUniversidad().getAno() != null ? p.getPeriodoUniversidad().getAno() : "")
                                        + " - " + p.getPeriodoUniversidad().getPeriodo()
                                        + (p.getPeriodoUniversidad().getCodigoPeriodo() != null && !p.getPeriodoUniversidad().getCodigoPeriodo().isBlank()
                                                ? " (" + p.getPeriodoUniversidad().getCodigoPeriodo() + ")"
                                                : ""))
                                : null
                ))
                .toList();

        return new ConvocatoriaFormularioDTO(
                convocatoria.getId(),
                convocatoria.getIdPeriodoUniversidad(),
                convocatoria.getIdRelacion(),
                convocatoria.getNombre(),
                convocatoria.getDescripcion(),
                convocatoria.getFechaInicio(),
                convocatoria.getFechaFin(),
                convocatoria.getEstado(),
                convocatoria.getEsIntersemestral(),
                vinculaciones,
                periodosExtension
        );
    }

    @Override
    @Transactional
    public void delete(Long id, ConvocatoriaFormularioDTO dto) {
        periodoExtensionRepository.deleteByIdConvocatoria(id);
        tipoVinculacionConvocatoriaRepository.deleteByIdConvocatoria(id);
        convocatoriaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void bulkDelete(List<ConvocatoriaFormularioDTO> listaConvocatorias) {
        if (listaConvocatorias == null) return;
        for (ConvocatoriaFormularioDTO dto : listaConvocatorias) {
            if (dto.id() != null) {
                delete(dto.id(), dto);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConvocatoriaDTO> findActivePreloadCalls(Long idPeriodoUniversidad) {
        if (idPeriodoUniversidad == null) {
            return convocatoriaRepository.findAllByOrderByIdDesc().stream()
                    .filter(c -> "1".equals(c.getEstado()))
                    .map(convocatoriaMapper::toListDto)
                    .collect(Collectors.toList());
        }
        return convocatoriaRepository.findByIdPeriodoUniversidad(idPeriodoUniversidad).stream()
                .filter(c -> "1".equals(c.getEstado()))
                .map(convocatoriaMapper::toListDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConvocatoriaDTO> findAssignableActivePreloadCalls(Long idPeriodoUniversidad) {
        return findActivePreloadCalls(idPeriodoUniversidad);
    }

    @Override
    @Transactional
    public void updateRelation(Long idConvocatoria, Long idRelacion) {
        ConvocatoriaEntity convocatoria = convocatoriaRepository.findById(idConvocatoria)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Convocatoria no encontrada"));
        convocatoria.setIdRelacion(idRelacion);
        convocatoria.setFechaCambio(new Date());
        convocatoria.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        convocatoriaRepository.save(convocatoria);
    }
}
