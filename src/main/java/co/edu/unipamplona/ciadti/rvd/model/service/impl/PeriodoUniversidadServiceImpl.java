package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.mapper.PeriodoUniversidadMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.PeriodoUniversidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.PeriodoUniversidadService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PeriodoUniversidadServiceImpl implements PeriodoUniversidadService {

    private final PeriodoUniversidadRepository periodoUniversidadRepository;
    private final PeriodoUniversidadMapper periodoUniversidadMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PeriodoUniversidadDTO> findUniversityPeriodList() {
        return periodoUniversidadMapper.toDtoList(periodoUniversidadRepository.findAllUniversityPeriods());
    }
}
