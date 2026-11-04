package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.TipoVinculacionConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoVinculacionListaProjection;

public interface TipoVinculacionConvocatoriaRepository extends JpaRepository<TipoVinculacionConvocatoriaEntity, Long> {
    List<TipoVinculacionConvocatoriaEntity> findByIdConvocatoria(Long idConvocatoria);

    @Query("SELECT t FROM TipoVinculacionConvocatoriaEntity t LEFT JOIN FETCH t.tipoVinculacion tv LEFT JOIN FETCH tv.clasificacionDocente LEFT JOIN FETCH tv.dedicacionDocente WHERE t.idConvocatoria = :idConvocatoria")
    List<TipoVinculacionConvocatoriaEntity> findByIdConvocatoriaWithVinculacion(@Param("idConvocatoria") Long idConvocatoria);

    @Modifying
    @Query("DELETE FROM TipoVinculacionConvocatoriaEntity t WHERE t.idConvocatoria = :idConvocatoria")
    void deleteByIdConvocatoria(@Param("idConvocatoria") Long idConvocatoria);
}
