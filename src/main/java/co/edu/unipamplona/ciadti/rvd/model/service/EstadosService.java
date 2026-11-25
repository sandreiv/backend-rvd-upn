package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoSiguienteFormularioDTO;

public interface EstadosService {

    List<EstadoDTO> listStates();

    EstadoDTO getStateById(Long id);

    void saveState(EstadoFormularioDTO dto);

    void updateState(Long id, EstadoFormularioDTO dto);

    void deleteState(Long id);

    void deleteStates(List<Long> ids);

    List<EstadoDTO> listNextStates(Long idEstadoOrigen);

    void saveNextStates(
            Long idEstadoOrigen,
            EstadoSiguienteFormularioDTO dto
    );

}