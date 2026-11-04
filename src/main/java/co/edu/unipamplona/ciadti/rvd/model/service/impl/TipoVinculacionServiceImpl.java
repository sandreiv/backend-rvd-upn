package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.unipamplona.ciadti.rvd.model.dto.ClasificacionDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DedicacionDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoHorasDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoVinculacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.enums.TipoHorasEnum;
import co.edu.unipamplona.ciadti.rvd.model.repository.ClasificacionDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DedicacionDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoVinculacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.TipoVinculacionService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoVinculacionServiceImpl implements TipoVinculacionService {

    private final TipoVinculacionRepository tipoVinculacionRepository;
    private final ClasificacionDocenteRepository clasificacionDocenteRepository;
    private final DedicacionDocenteRepository dedicacionDocenteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoVinculacionDTO> listActive() {
        return tipoVinculacionRepository.findAll().stream()
                .map(t -> new TipoVinculacionDTO(
                        t.getId(),
                        t.getIdClasificacionDocente(),
                        t.getIdDedicacionDocente(),
                        t.getMinimo(),
                        t.getMaximo(),
                        t.getFormaPago(),
                        t.getTipoContrato(),
                        t.getTipoHoras()
                ))
                .toList();
    }

    @Override
    @Transactional
    public TipoVinculacionDTO create(TipoVinculacionDTO dto) {
        TipoVinculacionEntity entity = new TipoVinculacionEntity();
        entity.setIdClasificacionDocente(dto.idClasificacionDocente() != null ? dto.idClasificacionDocente() : 1L);
        entity.setIdDedicacionDocente(dto.idDedicacionDocente() != null ? dto.idDedicacionDocente() : 1L);
        entity.setMinimo(dto.minimo());
        entity.setMaximo(dto.maximo());
        entity.setFormaPago(dto.formaPago());
        entity.setTipoContrato(dto.tipoContrato());
        entity.setTipoHoras(dto.tipoHoras() != null ? dto.tipoHoras().trim().toUpperCase() : null);
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        entity.setFechaCambio(new Date());

        TipoVinculacionEntity saved = tipoVinculacionRepository.save(entity);
        return new TipoVinculacionDTO(
                saved.getId(),
                saved.getIdClasificacionDocente(),
                saved.getIdDedicacionDocente(),
                saved.getMinimo(),
                saved.getMaximo(),
                saved.getFormaPago(),
                saved.getTipoContrato(),
                saved.getTipoHoras()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClasificacionDocenteDTO> listClasificacionesDocente() {
        return clasificacionDocenteRepository.findAllByOrderByDescripcionAsc().stream()
                .map(c -> new ClasificacionDocenteDTO(c.getId(), c.getDescripcion()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DedicacionDocenteDTO> listDedicacionesDocente() {
        return dedicacionDocenteRepository.findAllByOrderByDescripcionAsc().stream()
                .map(d -> new DedicacionDocenteDTO(d.getId(), d.getDescripcion()))
                .toList();
    }

    @Override
    public List<TipoHorasDTO> listTiposHoras() {
        return Arrays.stream(TipoHorasEnum.values())
                .map(e -> new TipoHorasDTO(e.name(), e.getDescripcion()))
                .toList();
    }
}
