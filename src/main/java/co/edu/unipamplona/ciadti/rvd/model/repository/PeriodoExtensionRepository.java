package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoExtensionEntity;

public interface PeriodoExtensionRepository extends JpaRepository<PeriodoExtensionEntity, Long> {

    List<PeriodoExtensionEntity> findByIdConvocatoria(Long idConvocatoria);

    @Query("SELECT p FROM PeriodoExtensionEntity p LEFT JOIN FETCH p.periodoUniversidad WHERE p.idConvocatoria = :idConvocatoria")
    List<PeriodoExtensionEntity> findByIdConvocatoriaWithPeriodo(@Param("idConvocatoria") Long idConvocatoria);

    @Modifying
    @Query("DELETE FROM PeriodoExtensionEntity p WHERE p.idConvocatoria = :idConvocatoria")
    void deleteByIdConvocatoria(@Param("idConvocatoria") Long idConvocatoria);
}
