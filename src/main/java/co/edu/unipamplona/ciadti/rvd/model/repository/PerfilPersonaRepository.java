package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.PerfilPersonaEntity;

public interface PerfilPersonaRepository
        extends JpaRepository<PerfilPersonaEntity, Long> {

    List<PerfilPersonaEntity> findAllByIdPerfil(
            Long idPerfil
    );

    boolean existsByIdPerfil(
            Long idPerfil
    );

    boolean existsByIdPersonaGeneral(
            Long idPersonaGeneral
    );

}