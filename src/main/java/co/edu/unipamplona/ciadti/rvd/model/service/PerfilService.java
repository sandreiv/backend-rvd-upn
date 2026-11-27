package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilEstadoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilPersonaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilPersonaFormularioDTO;

public interface PerfilService {

    List<PerfilDTO> listProfiles();

    PerfilDTO getProfileById(Long id);

    void saveProfile(PerfilFormularioDTO dto);

    void updateProfile(
            Long id,
            PerfilFormularioDTO dto
    );

    void deleteProfile(Long id);

    void deleteProfiles(List<Long> ids);

    /*
     * RELACIONAR PERFILES CON ESTADOS
     */

    List<EstadoDTO> listProfileStates(Long idPerfil);

    void saveProfileStates(
            Long idPerfil,
            PerfilEstadoFormularioDTO dto
    );

    /*
    * RELACIONAR PERFILES CON PERSONAS
    */

    List<PerfilPersonaDTO> searchPersons(String term);

    List<PerfilPersonaDTO> listProfilePersons(
            Long idPerfil
    );

    void saveProfilePersons(

            Long idPerfil,

            PerfilPersonaFormularioDTO dto

    );

}