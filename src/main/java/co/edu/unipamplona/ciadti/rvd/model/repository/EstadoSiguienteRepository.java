package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.EstadoSiguienteEntity;

public interface EstadoSiguienteRepository
        extends JpaRepository<EstadoSiguienteEntity, Long> {

    List<EstadoSiguienteEntity> findAllByIdEstadoOrigen(
            Long idEstadoOrigen
    );

}