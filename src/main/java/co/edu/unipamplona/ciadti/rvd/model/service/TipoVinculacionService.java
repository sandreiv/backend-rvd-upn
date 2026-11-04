package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;
import co.edu.unipamplona.ciadti.rvd.model.dto.ClasificacionDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DedicacionDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoHorasDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionDTO;

public interface TipoVinculacionService {
    List<TipoVinculacionDTO> listActive();
    TipoVinculacionDTO create(TipoVinculacionDTO dto);
    List<ClasificacionDocenteDTO> listClasificacionesDocente();
    List<DedicacionDocenteDTO> listDedicacionesDocente();
    List<TipoHorasDTO> listTiposHoras();
}
