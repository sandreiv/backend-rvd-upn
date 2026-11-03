package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.FechasConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaEstadoService;
import co.edu.unipamplona.ciadti.rvd.util.FechasConvocatoriaCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConvocatoriaEstadoServiceImpl implements ConvocatoriaEstadoService {

    private final ConvocatoriaRepository convocatoriaRepository;
    private final FechasConvocatoriaRepository fechasConvocatoriaRepository;

    @Override
    @Transactional
    public void syncEstadoConvocatoria(Long idConvocatoria) {
        if (idConvocatoria == null) {
            return;
        }

        ConvocatoriaEntity convocatoria = convocatoriaRepository.findById(idConvocatoria).orElse(null);
        if (convocatoria == null) {
            log.warn("syncEstadoConvocatoria ===> Convocatoria no encontrada. idConvocatoria={}", idConvocatoria);
            return;
        }

        FechasConvocatoriaEntity fechaConvocatoria = convocatoriaRepository.findFechaCnvByConvocatoriaId(idConvocatoria);
        boolean convocatoriaVencida = fechaConvocatoria != null
                && FechasConvocatoriaCalculator.isVencida(fechaConvocatoria.getFechaFin());
        if (!convocatoriaVencida) {
            return;
        }

        String nuevoEstado = "0";
        if (Objects.equals(convocatoria.getEstado(), nuevoEstado)) {
            return;
        }

        Date ahora = new Date();
        convocatoriaRepository.updateEstado(nuevoEstado, ahora, idConvocatoria);
        convocatoria.setEstado(nuevoEstado);
        convocatoria.setFechaCambio(ahora);
        log.info("syncEstadoConvocatoria ===> Convocatoria vencida. idConvocatoria={}", idConvocatoria);
    }

    @Override
    @Transactional
    public void syncEstadoConvocatoriaByFecha(Long idFechasConvocatoria) {
        if (idFechasConvocatoria == null) {
            return;
        }
        fechasConvocatoriaRepository.findById(idFechasConvocatoria)
                .map(FechasConvocatoriaEntity::getIdConvocatoria)
                .ifPresent(this::syncEstadoConvocatoria);
    }

    @Override
    @Transactional
    public void syncEstadosConvocatoriasConRestricciones() {
        log.debug("syncEstadosConvocatoriasConRestricciones ===> Sin restricciones por coordinación en RVDIES");
    }
}
