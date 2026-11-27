package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.PerfilEstadoEntity;

public interface PerfilEstadoRepository
        extends JpaRepository<PerfilEstadoEntity, Long> {

    List<PerfilEstadoEntity> findAllByIdPerfil(Long idPerfil);

    boolean existsByIdPerfil(Long idPerfil);

    boolean existsByIdEstado(Long idEstado);

}