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
 * 19/11/2026 - Listado de todos los centros de costo.
 * 19/11/2026 - Consulta y validación de centros de costo para proyectos incluyendo coordinación, programas y subcoordinaciones.
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
            SELECT
                CECO.CECO_ID AS id,
                CECO.CECO_DESCRIPCION AS label,
                CECO.CECO_CODIGO AS codigo
            FROM CONTABLEV3.CENTROCOSTO CECO
            WHERE UPPER(CECO.CECO_DESCRIPCION) LIKE UPPER('%' || :texto || '%')
                OR UPPER(CECO.CECO_CODIGO) LIKE UPPER('%' || :texto || '%')
            ORDER BY CECO.CECO_DESCRIPCION, CECO.CECO_ID
            FETCH FIRST 25 ROWS ONLY
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findByTexto(@Param("texto") String texto);

    @Query(value = """
            SELECT DISTINCT
                CECO.CECO_ID AS id,
                CECO.CECO_DESCRIPCION AS label,
                CECO.CECO_CODIGO AS codigo
            FROM CONTABLEV3.CENTROCOSTO CECO
            INNER JOIN RVDIES.CENTROCOSTOCOORDINACION CCCO ON CCCO.CECO_ID = CECO.CECO_ID
            WHERE CCCO.COOR_ID = :idCoordinacion
              AND (CCCO.CCCO_ESTADO = '1' OR CCCO.CCCO_ESTADO IS NULL)
              AND CECO.CECO_DESCRIPCION IS NOT NULL
            ORDER BY CECO.CECO_DESCRIPCION
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findByCoordinacionId(@Param("idCoordinacion") Long idCoordinacion);

    @Query(value = """
            SELECT DISTINCT
                CECO.CECO_ID AS id,
                CECO.CECO_DESCRIPCION AS label,
                CECO.CECO_CODIGO AS codigo
            FROM CONTABLEV3.CENTROCOSTO CECO
            INNER JOIN RVDIES.CENTROCOSTOCOORDINACION CCCO ON CCCO.CECO_ID = CECO.CECO_ID
            WHERE CCCO.COOR_ID IN (
                SELECT C.COOR_ID
                FROM RVDIES.COORDINACIONES C
                START WITH C.COOR_ID = :idCoordinacion
                CONNECT BY PRIOR C.COOR_ID = C.COOR_IDPADRE
            )
              AND (CCCO.CCCO_ESTADO IN ('1', 'S', 'A') OR CCCO.CCCO_ESTADO IS NULL)
              AND CECO.CECO_DESCRIPCION IS NOT NULL
            ORDER BY CECO.CECO_DESCRIPCION
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findCentrosCostoByCoordinacionIdIncludingChildren(
            @Param("idCoordinacion") Long idCoordinacion
    );

    @Query(value = """
            SELECT DISTINCT
                CECO.CECO_ID AS id,
                CECO.CECO_DESCRIPCION AS label,
                CECO.CECO_CODIGO AS codigo
            FROM CONTABLEV3.CENTROCOSTO CECO
            WHERE CECO.CECO_DESCRIPCION IS NOT NULL
              AND (
                (:idCoordinacion IS NOT NULL AND (
                    CECO.CECO_ID IN (
                        SELECT CCCO.CECO_ID
                        FROM RVDIES.CENTROCOSTOCOORDINACION CCCO
                        WHERE (CCCO.COOR_ID = :idCoordinacion
                               OR CCCO.COOR_ID IN (SELECT C.COOR_ID FROM RVDIES.COORDINACIONES C WHERE C.COOR_IDPADRE = :idCoordinacion))
                          AND (CCCO.CCCO_ESTADO = '1' OR CCCO.CCCO_ESTADO IS NULL)
                    )
                    OR
                    CECO.CECO_ID IN (
                        SELECT PRCO.CECO_ID
                        FROM RVDIES.PROGRAMACOORDINACION PRCO
                        WHERE (PRCO.COOR_ID = :idCoordinacion
                               OR PRCO.COOR_ID IN (SELECT C.COOR_ID FROM RVDIES.COORDINACIONES C WHERE C.COOR_IDPADRE = :idCoordinacion))
                          AND PRCO.CECO_ID IS NOT NULL
                          AND (PRCO.PRCO_ESTADO = '1' OR PRCO.PRCO_ESTADO IS NULL)
                    )
                ))
                OR
                (:idProyecto IS NOT NULL AND CECO.CECO_ID IN (
                    SELECT PRCO.CECO_ID
                    FROM RVDIES.ESTUDIANTES ESTU
                    INNER JOIN ACADEMICO.ESTUDIANTEPENSUM ESTP
                        ON (ESTP.ESTP_ID = ESTU.ESTP_ID OR (ESTU.ESTP_ID IS NULL AND ESTP.PEGE_ID = ESTU.PEGE_ID))
                    INNER JOIN ACADEMICO.UNIDADPROGRAMA UNPR
                        ON UNPR.UNPR_ID = ESTP.UNPR_ID
                    INNER JOIN RVDIES.PROGRAMACOORDINACION PRCO
                        ON PRCO.PROG_ID = UNPR.PROG_ID
                    WHERE ESTU.PROY_ID = :idProyecto
                      AND (PRCO.PRCO_ESTADO = '1' OR PRCO.PRCO_ESTADO IS NULL)
                ))
              )
            ORDER BY CECO.CECO_DESCRIPCION
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findCostCentersByCoordinacionAndProject(@Param("idCoordinacion") Long idCoordinacion, @Param("idProyecto") Long idProyecto);

    @Query(value = """
            SELECT COUNT(1)
            FROM CONTABLEV3.CENTROCOSTO CECO
            WHERE CECO.CECO_ID = :idCentroCosto
              AND (
                (:idCoordinacion IS NOT NULL AND (
                    CECO.CECO_ID IN (
                        SELECT CCCO.CECO_ID
                        FROM RVDIES.CENTROCOSTOCOORDINACION CCCO
                        WHERE (CCCO.COOR_ID = :idCoordinacion
                               OR CCCO.COOR_ID IN (SELECT C.COOR_ID FROM RVDIES.COORDINACIONES C WHERE C.COOR_IDPADRE = :idCoordinacion))
                          AND (CCCO.CCCO_ESTADO = '1' OR CCCO.CCCO_ESTADO IS NULL)
                    )
                    OR
                    CECO.CECO_ID IN (
                        SELECT PRCO.CECO_ID
                        FROM RVDIES.PROGRAMACOORDINACION PRCO
                        WHERE (PRCO.COOR_ID = :idCoordinacion
                               OR PRCO.COOR_ID IN (SELECT C.COOR_ID FROM RVDIES.COORDINACIONES C WHERE C.COOR_IDPADRE = :idCoordinacion))
                          AND PRCO.CECO_ID IS NOT NULL
                          AND (PRCO.PRCO_ESTADO = '1' OR PRCO.PRCO_ESTADO IS NULL)
                    )
                ))
                OR
                (:idProyecto IS NOT NULL AND CECO.CECO_ID IN (
                    SELECT PRCO.CECO_ID
                    FROM RVDIES.ESTUDIANTES ESTU
                    INNER JOIN ACADEMICO.ESTUDIANTEPENSUM ESTP
                        ON (ESTP.ESTP_ID = ESTU.ESTP_ID OR (ESTU.ESTP_ID IS NULL AND ESTP.PEGE_ID = ESTU.PEGE_ID))
                    INNER JOIN ACADEMICO.UNIDADPROGRAMA UNPR
                        ON UNPR.UNPR_ID = ESTP.UNPR_ID
                    INNER JOIN RVDIES.PROGRAMACOORDINACION PRCO
                        ON PRCO.PROG_ID = UNPR.PROG_ID
                    WHERE ESTU.PROY_ID = :idProyecto
                      AND (PRCO.PRCO_ESTADO = '1' OR PRCO.PRCO_ESTADO IS NULL)
                ))
              )
            """, nativeQuery = true)
    long countValidCostCenterForProject(@Param("idCoordinacion") Long idCoordinacion, @Param("idProyecto") Long idProyecto, @Param("idCentroCosto") Long idCentroCosto);
}
