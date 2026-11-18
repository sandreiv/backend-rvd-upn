/**
 * Aplicación: rvd
 * Archivo: CentroCostoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 17/11/2026 - Consulta de centros de costo por coordinación y validación de pertenencia
 * 18/11/2026 - Consulta de centros de costo asociados a programas de estudiantes del proyecto por PEGE_ID / ESTP_ID
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.CentroCostoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;

public interface CentroCostoRepository extends JpaRepository<CentroCostoEntity, Long> {

    @Query(value = """
            SELECT
                CECO.CECO_ID AS id,
                CECO.CECO_DESCRIPCION AS label,
                CECO.CECO_CODIGO AS codigo
            FROM CONTABLEV3.CENTROCOSTO CECO
            WHERE CECO.CECO_DESCRIPCION IS NOT NULL
            ORDER BY CECO.CECO_DESCRIPCION
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findAdministrationOptions();

    @Query(value = """
            SELECT DISTINCT
                CECO.CECO_ID AS id,
                CECO.CECO_DESCRIPCION AS label,
                CECO.CECO_CODIGO AS codigo
            FROM CONTABLEV3.CENTROCOSTO CECO
            INNER JOIN RVDIES.CENTROCOSTOCOORDINACION CCCO ON CCCO.CECO_ID = CECO.CECO_ID
            WHERE CCCO.COOR_ID = :idCoordinacion
              AND (CCCO.CCCO_ESTADO = '1' OR CCCO.CCCO_ESTADO = 'S' OR CCCO.CCCO_ESTADO = 'A' OR CCCO.CCCO_ESTADO IS NULL)
              AND CECO.CECO_DESCRIPCION IS NOT NULL
            ORDER BY CECO.CECO_DESCRIPCION
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findByCoordinacionId(@Param("idCoordinacion") Long idCoordinacion);

    @Query(value = """
            SELECT COUNT(CCCO.CCCO_ID)
            FROM RVDIES.CENTROCOSTOCOORDINACION CCCO
            WHERE CCCO.COOR_ID = :idCoordinacion
              AND CCCO.CECO_ID = :idCentroCosto
              AND (CCCO.CCCO_ESTADO = '1' OR CCCO.CCCO_ESTADO = 'S' OR CCCO.CCCO_ESTADO = 'A' OR CCCO.CCCO_ESTADO IS NULL)
            """, nativeQuery = true)
    long countByCoordinacionIdAndCentroCostoId(
            @Param("idCoordinacion") Long idCoordinacion,
            @Param("idCentroCosto") Long idCentroCosto
    );

    @Query(value = """
            SELECT DISTINCT
                CECO.CECO_ID AS id,
                CECO.CECO_DESCRIPCION AS label,
                CECO.CECO_CODIGO AS codigo
            FROM RVDIES.ESTUDIANTES ESTU
            INNER JOIN ACADEMICO.ESTUDIANTEPENSUM ESTP
                ON (ESTP.ESTP_ID = ESTU.ESTP_ID OR (ESTU.ESTP_ID IS NULL AND ESTP.PEGE_ID = ESTU.PEGE_ID))
            INNER JOIN ACADEMICO.UNIDADPROGRAMA UNPR
                ON UNPR.UNPR_ID = ESTP.UNPR_ID
            INNER JOIN RVDIES.PROGRAMACOORDINACION PRCO
                ON PRCO.PROG_ID = UNPR.PROG_ID
            INNER JOIN CONTABLEV3.CENTROCOSTO CECO
                ON CECO.CECO_ID = PRCO.CECO_ID
            WHERE ESTU.PROY_ID = :idProyecto
              AND (PRCO.PRCO_ESTADO = '1' OR PRCO.PRCO_ESTADO = 'S' OR PRCO.PRCO_ESTADO = 'A' OR PRCO.PRCO_ESTADO IS NULL)
              AND CECO.CECO_DESCRIPCION IS NOT NULL
            ORDER BY CECO.CECO_DESCRIPCION
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findCostCentersByProjectStudents(@Param("idProyecto") Long idProyecto);

    @Query(value = """
            SELECT COUNT(DISTINCT CECO.CECO_ID)
            FROM RVDIES.ESTUDIANTES ESTU
            INNER JOIN ACADEMICO.ESTUDIANTEPENSUM ESTP
                ON (ESTP.ESTP_ID = ESTU.ESTP_ID OR (ESTU.ESTP_ID IS NULL AND ESTP.PEGE_ID = ESTU.PEGE_ID))
            INNER JOIN ACADEMICO.UNIDADPROGRAMA UNPR
                ON UNPR.UNPR_ID = ESTP.UNPR_ID
            INNER JOIN RVDIES.PROGRAMACOORDINACION PRCO
                ON PRCO.PROG_ID = UNPR.PROG_ID
            INNER JOIN CONTABLEV3.CENTROCOSTO CECO
                ON CECO.CECO_ID = PRCO.CECO_ID
            WHERE ESTU.PROY_ID = :idProyecto
              AND CECO.CECO_ID = :idCentroCosto
              AND (PRCO.PRCO_ESTADO = '1' OR PRCO.PRCO_ESTADO = 'S' OR PRCO.PRCO_ESTADO = 'A' OR PRCO.PRCO_ESTADO IS NULL)
            """, nativeQuery = true)
    long countByProjectStudentsAndCentroCostoId(
            @Param("idProyecto") Long idProyecto,
            @Param("idCentroCosto") Long idCentroCosto
    );
}
