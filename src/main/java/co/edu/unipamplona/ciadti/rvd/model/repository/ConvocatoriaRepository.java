package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;

public interface ConvocatoriaRepository extends JpaRepository<ConvocatoriaEntity, Long> {

    List<ConvocatoriaEntity> findAllByOrderByIdDesc();

    List<ConvocatoriaEntity> findByIdPeriodoUniversidad(Long idPeriodoUniversidad);

    List<ConvocatoriaEntity> findByIdPeriodoUniversidadAndEstado(Long idPeriodoUniversidad, String estado);

    @Query("SELECT c FROM ConvocatoriaEntity c WHERE c.periodoUniversidad.ano = :year AND c.periodoUniversidad.periodo = '1' ORDER BY c.id")
    List<ConvocatoriaEntity> findByPeriodoYear(@Param("year") Long year);

    @Query("SELECT c.periodoUniversidad FROM ConvocatoriaEntity c WHERE c.id = :id")
    PeriodoUniversidadEntity findPeriodoEntityByConvocatoriaId(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ConvocatoriaEntity c SET c.estado = :estado, c.fechaCambio = :fechaCambio WHERE c.id = :id")
    int updateEstado(@Param("estado") String estado, @Param("fechaCambio") Date fechaCambio, @Param("id") Long id);

    @Modifying
    @Query("UPDATE ConvocatoriaEntity c SET c.idRelacion = :idRelacion, c.fechaCambio = :fechaCambio WHERE c.id = :id")
    int updateIdRelacion(@Param("idRelacion") Long idRelacion, @Param("fechaCambio") Date fechaCambio, @Param("id") Long id);

    @Procedure(name = "ConvocatoriaEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(@Param("P_CONV_ID") Long id, @Param("P_CONV_REGISTRADOPOR") String registradoPor);

    boolean existsByIdAndIdPeriodoUniversidad(Long id, Long idPeriodoUniversidad);

    @Query(value = "SELECT * FROM RVDIES.FECHASCONVOCATORIA WHERE CONV_ID = :id AND FECO_CODIGO = 'CNV'", nativeQuery = true)
    FechasConvocatoriaEntity findFechaCnvByConvocatoriaId(@Param("id") Long id);
}
