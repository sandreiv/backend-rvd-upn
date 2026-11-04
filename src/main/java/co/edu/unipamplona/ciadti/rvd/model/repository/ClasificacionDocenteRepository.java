package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unipamplona.ciadti.rvd.model.entity.ClasificacionDocenteEntity;

public interface ClasificacionDocenteRepository extends JpaRepository<ClasificacionDocenteEntity, Long> {
    List<ClasificacionDocenteEntity> findAllByOrderByDescripcionAsc();
}
