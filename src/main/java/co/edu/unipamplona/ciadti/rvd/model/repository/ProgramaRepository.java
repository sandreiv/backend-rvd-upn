package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ProgramaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProgramaListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;

public interface ProgramaRepository extends JpaRepository<ProgramaEntity, Long> {

    @Query(value = """
            SELECT DISTINCT
                prog.PROG_ID AS id,
                prog.PROG_NOMBRE AS nombre,
                ceco.CECO_ID AS idCentroCosto,
                ceco.CECO_DESCRIPCION AS descripcionCentroCosto
            FROM RVD.ASOCIACIONCOORDINACION asco
            INNER JOIN ACADEMICO.PROGRAMA prog
                ON prog.PROG_ID = asco.PROG_ID
            INNER JOIN ACADEMICO.UNIDADPROGRAMA unpr
                ON prog.PROG_ID = unpr.PROG_ID
            INNER JOIN ACADEMICO.MODALIDAD moda
                ON prog.MODA_ID = moda.MODA_ID
            INNER JOIN ACADEMICO.NIVELEDUCATIVO nied
                ON moda.NIED_ID = nied.NIED_ID
            LEFT JOIN CONTABLEV3.CENTROCOSTO ceco
                ON ceco.CECO_ID = asco.CECO_ID
            WHERE asco.COOR_ID = :idCoordinacion
                AND asco.MATE_CODIGOMATERIA IS NULL
                AND asco.PROG_ID IS NOT NULL
                AND unpr.UNID_ID = :idUnidadRegional
                AND nied.NIED_ID = :idNivelEducativo
            ORDER BY prog.PROG_NOMBRE
            """, nativeQuery = true)
    List<ProgramaListadoProjection> findByCoordinacionUnidadRegionalAndNivelEducativo(
            @Param("idCoordinacion") Long idCoordinacion,
            @Param("idUnidadRegional") Long idUnidadRegional,
            @Param("idNivelEducativo") Long idNivelEducativo);

    @Query(value = """
            SELECT DISTINCT
                prog.PROG_ID AS id,
                prog.PROG_NOMBRE AS nombre,
                CAST(NULL AS NUMBER) AS idCentroCosto,
                CAST(NULL AS VARCHAR2(255)) AS descripcionCentroCosto
            FROM ACADEMICO.UNIDADPROGRAMA unpr
            INNER JOIN ACADEMICO.PROGRAMA prog
                ON prog.PROG_ID = unpr.PROG_ID
            INNER JOIN ACADEMICO.MODALIDAD moda
                ON prog.MODA_ID = moda.MODA_ID
            INNER JOIN ACADEMICO.NIVELEDUCATIVO nied
                ON moda.NIED_ID = nied.NIED_ID
            WHERE unpr.UNID_ID = :idUnidadRegional
                AND nied.NIED_ID = :idNivelEducativo
            ORDER BY prog.PROG_NOMBRE
            """, nativeQuery = true)
    List<ProgramaListadoProjection> findByUnidadRegionalAndNivelEducativo(
            @Param("idUnidadRegional") Long idUnidadRegional,
            @Param("idNivelEducativo") Long idNivelEducativo);

    @Query(value = """
            SELECT DISTINCT
                PROG.PROG_ID AS id,
                PROG.PROG_NOMBRE AS nombre,
                CECO.CECO_ID AS idCentroCosto,
                CECO.CECO_DESCRIPCION AS descripcionCentroCosto
            FROM RVDIES.PROGRAMACOORDINACION PRCO
            INNER JOIN RVDIES.COORDINACIONES COOR
                ON COOR.COOR_ID = PRCO.COOR_ID
            INNER JOIN ACADEMICO.PROGRAMA PROG
                ON PROG.PROG_ID = PRCO.PROG_ID
            INNER JOIN ACADEMICO.UNIDADPROGRAMA UNPR
                ON UNPR.PROG_ID = PROG.PROG_ID
                AND UNPR.UNID_ID = :idUnidadRegional
            LEFT JOIN CONTABLEV3.CENTROCOSTO CECO
                ON CECO.CECO_ID = PRCO.CECO_ID
            WHERE PRCO.COOR_ID = :idCoordinacion
                AND COOR.UNID_IDREGIONAL = :idUnidadRegional
                AND PRCO.PROG_ID IS NOT NULL
                AND NVL(PRCO.PRCO_ESTADO, '1') = '1'
                AND (
                    NOT EXISTS (
                        SELECT 1
                        FROM RVDIES.RESTRICCIONPROGRAMAS REPR
                        WHERE REPR.PRCO_ID = PRCO.PRCO_ID
                            AND REPR.REPR_ESTADO = '1'
                    )
                    OR EXISTS (
                        SELECT 1
                        FROM RVDIES.RESTRICCIONPROGRAMAS REPR
                        INNER JOIN RVDIES.PERSONACOORDINACION PECO
                            ON PECO.PECO_ID = REPR.PECO_ID
                        WHERE REPR.PRCO_ID = PRCO.PRCO_ID
                            AND REPR.REPR_ESTADO = '1'
                            AND PECO.PEGE_ID = :idPersona
                            AND PECO.COOR_ID = PRCO.COOR_ID
                            AND NVL(PECO.PECO_ESTADO, '1') = '1'
                    )
                )
            ORDER BY PROG.PROG_NOMBRE
            """, nativeQuery = true)
    List<ProgramaListadoProjection> findByCoordinacionAndUnidadRegional(
            @Param("idCoordinacion") Long idCoordinacion,
            @Param("idUnidadRegional") Long idUnidadRegional,
            @Param("idPersona") Long idPersona);



    @Query(value = """
            SELECT
                PROG.PROG_ID AS id,
                PROG.PROG_NOMBRE AS label,
                PROG.PROG_CODIGOPROGRAMA AS codigo
            FROM ACADEMICO.PROGRAMA PROG
            WHERE PROG.PROG_NOMBRE IS NOT NULL
            ORDER BY PROG.PROG_NOMBRE
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findAdministrationOptions();        


}
