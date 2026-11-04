package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoVinculacionEntity;

public interface TipoVinculacionRepository extends JpaRepository<TipoVinculacionEntity, Long> {

    @Query("SELECT t FROM TipoVinculacionEntity t LEFT JOIN FETCH t.clasificacionDocente LEFT JOIN FETCH t.dedicacionDocente ORDER BY t.id ASC")
    List<TipoVinculacionEntity> findAllWithDetails();
}
