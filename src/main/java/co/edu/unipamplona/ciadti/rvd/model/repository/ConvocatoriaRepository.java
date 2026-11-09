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

    @Query("SELECT c FROM ConvocatoriaEntity c LEFT JOIN FETCH c.periodoUniversidad p LEFT JOIN FETCH p.tipoPeriodoAcademico ORDER BY c.id DESC")
    List<ConvocatoriaEntity> findAllByOrderByIdDesc();

    @Query("SELECT c FROM ConvocatoriaEntity c LEFT JOIN FETCH c.periodoUniversidad p LEFT JOIN FETCH p.tipoPeriodoAcademico WHERE c.idPeriodoUniversidad = :idPeriodoUniversidad ORDER BY c.id DESC")
    List<ConvocatoriaEntity> findByIdPeriodoUniversidad(@Param("idPeriodoUniversidad") Long idPeriodoUniversidad);

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
    @Query(value = """
            SELECT
                CONV.CONV_ID,
                CONV.PEUN_ID,
                CONV.CONV_IDRELACION,
                CONV.CONV_NOMBRE,
                CONV.CONV_DESCRIPCION,
                CONV.CONV_ESTADO,
                CONV.CONV_FECHAINICIO,
                CONV.CONV_FECHAFIN,
                CONV.CONV_ESINTERSEMESTRAL,
                CONV.CONV_REGISTRADOPOR,
                CONV.CONV_FECHACAMBIO
            FROM RVDIES.CONVOCATORIA CONV
            WHERE CONV.CONV_ESTADO = '1'
            AND CONV.PEUN_ID = :idPeriodoUniversidad
            ORDER BY CONV.CONV_ID
            """, nativeQuery = true)
    List<ConvocatoriaEntity> findActivePreloadCalls(
            @Param("idPeriodoUniversidad") Long idPeriodoUniversidad);

    @Query(value = """
            SELECT
                CONV.CONV_ID,
                CONV.PEUN_ID,
                CONV.NIED_ID,
                CONV.PEGE_IDAUTORIZA,
                CONV.CONV_NOMBRE,
                CONV.CONV_DESCRIPCION,
                CONV.CONV_ESTADO,
                CONV.CONV_REGISTRADOPOR,
                CONV.CONV_FECHACAMBIO,
                CONV.CONV_IDRELACION,
                CONV.CONV_CONTRATACION
            FROM RVD.CONVOCATORIA CONV
            WHERE CONV.CONV_ESTADO = '1'
            AND CONV.PEUN_ID = :idPeriodoUniversidad
            AND NOT EXISTS (
                SELECT 1
                FROM RVD.RESTRICCIONXCOORDINACION REXC
                INNER JOIN RVD.FECHASCONVOCATORIA FECO
                    ON FECO.FECO_ID = REXC.FECO_ID
                WHERE FECO.CONV_ID = CONV.CONV_ID
                    AND REXC.REXC_ESTADO = '1'
                    AND TRUNC(REXC.REXC_FECHAFIN) >= TRUNC(SYSDATE)
            )
            ORDER BY CONV.CONV_ID
            """, nativeQuery = true)
    List<ConvocatoriaEntity> findAssignableActivePreloadCalls(
            @Param("idPeriodoUniversidad") Long idPeriodoUniversidad);

    @Query(value = "SELECT * FROM RVDIES.FECHASCONVOCATORIA WHERE CONV_ID = :id AND FECO_CODIGO = 'CNV'", nativeQuery = true)
    FechasConvocatoriaEntity findFechaCnvByConvocatoriaId(@Param("id") Long id);
}
