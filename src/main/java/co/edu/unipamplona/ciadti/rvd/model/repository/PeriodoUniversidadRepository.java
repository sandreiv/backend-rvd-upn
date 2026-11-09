package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;

public interface PeriodoUniversidadRepository
        extends JpaRepository<PeriodoUniversidadEntity, Long> {

    @Query("""
            SELECT p FROM PeriodoUniversidadEntity p
            LEFT JOIN FETCH p.tipoPeriodoAcademico t
            ORDER BY p.ano DESC, p.periodo
            """)
    List<PeriodoUniversidadEntity> findAllUniversityPeriods();
}
