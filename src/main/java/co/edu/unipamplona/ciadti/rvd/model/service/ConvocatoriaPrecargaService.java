package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaFormularioDTO;

public interface ConvocatoriaPrecargaService {

    List<ConvocatoriaDTO> findCallListWithDates(Long idPeriodoUniversidad);

    List<ConvocatoriaDTO> findCallListByFirstPeriodByYear(Long year);

    void save(ConvocatoriaFormularioDTO dto);

    void update(Long id, ConvocatoriaFormularioDTO dto);

    ConvocatoriaFormularioDTO findCallDetail(Long id);

    void delete(Long id, ConvocatoriaFormularioDTO dto);

    void bulkDelete(List<ConvocatoriaFormularioDTO> listaConvocatorias);

    List<ConvocatoriaDTO> findActivePreloadCalls(Long idPeriodoUniversidad);

    List<ConvocatoriaDTO> findAssignableActivePreloadCalls(Long idPeriodoUniversidad);

    void updateRelation(Long idConvocatoria, Long idRelacion);
}
