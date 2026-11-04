package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unipamplona.ciadti.rvd.model.entity.DedicacionDocenteEntity;

public interface DedicacionDocenteRepository extends JpaRepository<DedicacionDocenteEntity, Long> {
    List<DedicacionDocenteEntity> findAllByOrderByDescripcionAsc();
}
