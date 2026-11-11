package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.GrupoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.GrupoCargaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.GrupoListadoProjection;

public interface GrupoRepository extends JpaRepository<GrupoEntity, Long> {

    @Query(value = """
            SELECT
                grup.GRUP_ID AS id,
                grup.GRUP_NOMBRE AS nombre,
                grup.GRUP_CAPACIDAD AS capacidad
            FROM ACADEMICO.GRUPO grup
            WHERE grup.MATE_CODIGOMATERIA = :codigoMateria
                AND grup.PEUN_ID = :idPeriodoUniversidad
            ORDER BY grup.GRUP_NOMBRE
            """, nativeQuery = true)
    List<GrupoListadoProjection> findByCodigoMateriaAndIdPeriodoUniversidad(
            @Param("codigoMateria") String codigoMateria,
            @Param("idPeriodoUniversidad") Long idPeriodoUniversidad);

    @Query(value = """
            SELECT DISTINCT grup.MATE_CODIGOMATERIA
            FROM ACADEMICO.GRUPO grup
            WHERE grup.MATE_CODIGOMATERIA IN (:codigosMateria)
            """, nativeQuery = true)
    List<String> findCodigosMateriaConGrupo(
            @Param("codigosMateria") List<String> codigosMateria);

    @Query(value = """
            SELECT
                GRUP.GRUP_ID AS id,
                GRUP.GRUP_NOMBRE AS nombre,
                GRUP.GRUP_CAPACIDAD AS capacidad,
                PEUN.PEUN_ID AS idPeriodoUniversidad,
                PEUN.PEUN_ANO AS anio,
                PEUN.PEUN_PERIODO AS periodo,
                PEUN.PEUN_CODIGOPERIODO AS codigoPeriodo
            FROM ACADEMICO.GRUPO GRUP
            INNER JOIN ACADEMICO.PERIODOUNIVERSIDAD PEUN
                ON PEUN.PEUN_ID = GRUP.PEUN_ID
            WHERE GRUP.MATE_CODIGOMATERIA = :codigoMateria
                AND GRUP.PEUN_ID IN (
                    SELECT CONV.PEUN_ID
                    FROM RVDIES.CONVOCATORIA CONV
                    WHERE CONV.CONV_ID = :idConvocatoria
                    UNION
                    SELECT PEEX.PEUN_ID
                    FROM RVDIES.PERIODOEXTENSION PEEX
                    WHERE PEEX.CONV_ID = :idConvocatoria
                )
            ORDER BY PEUN.PEUN_ANO, PEUN.PEUN_PERIODO, GRUP.GRUP_NOMBRE
            """, nativeQuery = true)
    List<GrupoCargaProjection> findByMateriaAndConvocatoria(
            @Param("codigoMateria") String codigoMateria,
            @Param("idConvocatoria") Long idConvocatoria);
}
