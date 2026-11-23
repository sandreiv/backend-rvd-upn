/**
 * Aplicación: rvd
 * Archivo: EstudiantesRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/11/2026
 * Modificaciones:
 * 18/11/2026 - Conexión directa con pensum y programa académico mediante PEGE_ID
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.EstudiantesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.EstudianteBusquedaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.EstudianteProyectoListaProjection;

public interface EstudiantesRepository extends JpaRepository<EstudiantesEntity, Long> {

    @Query(value = """
            SELECT
                ESTU.ESTU_ID AS id,
                ESTU.PROY_ID AS idProyecto,
                ESTU.PEGE_ID AS idPersonaGeneral,
                ESTU.ESTP_ID AS idEstudiantePensum,
                PEGE.PEGE_DOCUMENTOIDENTIDAD AS documento,
                TRIM(
                    PENG.PENG_PRIMERNOMBRE || ' ' ||
                    NVL(PENG.PENG_SEGUNDONOMBRE, '') || ' ' ||
                    PENG.PENG_PRIMERAPELLIDO || ' ' ||
                    NVL(PENG.PENG_SEGUNDOAPELLIDO, '')
                ) AS nombreCompleto,
                PROG.PROG_ID AS idPrograma,
                PROG.PROG_NOMBRE AS nombrePrograma,
                CECO.CECO_ID AS idCentroCosto,
                CECO.CECO_DESCRIPCION AS nombreCentroCosto
            FROM RVDIES.ESTUDIANTES ESTU
            INNER JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = ESTU.PEGE_ID
            INNER JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
            LEFT JOIN ACADEMICO.ESTUDIANTEPENSUM ESTP
                ON (ESTP.ESTP_ID = ESTU.ESTP_ID OR (ESTU.ESTP_ID IS NULL AND ESTP.PEGE_ID = ESTU.PEGE_ID))
            LEFT JOIN ACADEMICO.UNIDADPROGRAMA UNPR
                ON UNPR.UNPR_ID = ESTP.UNPR_ID
            LEFT JOIN ACADEMICO.PROGRAMA PROG
                ON PROG.PROG_ID = UNPR.PROG_ID
            LEFT JOIN RVDIES.PROGRAMACOORDINACION PRCO
                ON PRCO.PROG_ID = PROG.PROG_ID
                AND (PRCO.PRCO_ESTADO IN ('1', 'S', 'A') OR PRCO.PRCO_ESTADO IS NULL)
            LEFT JOIN CONTABLEV3.CENTROCOSTO CECO
                ON CECO.CECO_ID = PRCO.CECO_ID
            WHERE ESTU.PROY_ID = :idProyecto
            ORDER BY PENG.PENG_PRIMERAPELLIDO, PENG.PENG_PRIMERNOMBRE
            """, nativeQuery = true)
    List<EstudianteProyectoListaProjection> findStudentsByProjectId(@Param("idProyecto") Long idProyecto);

    @Query(value = """
            SELECT COUNT(1)
            FROM RVDIES.ESTUDIANTES ESTU
            WHERE ESTU.PROY_ID = :idProyecto
              AND ESTU.PEGE_ID = :idPersonaGeneral
            """, nativeQuery = true)
    long countByProyectoIdAndPersonaGeneralId(
            @Param("idProyecto") Long idProyecto,
            @Param("idPersonaGeneral") Long idPersonaGeneral
    );

    boolean existsByIdProyectoIn(List<Long> ids);

    @Query(value = """
            SELECT
                T.idPersonaGeneral,
                T.documento,
                T.nombreCompleto,
                T.idEstudiantePensum,
                T.idPrograma,
                T.nombrePrograma
            FROM (
                SELECT
                    PEGE.PEGE_ID AS idPersonaGeneral,
                    PEGE.PEGE_DOCUMENTOIDENTIDAD AS documento,
                    TRIM(
                        PENG.PENG_PRIMERNOMBRE || ' ' ||
                        NVL(PENG.PENG_SEGUNDONOMBRE, '') || ' ' ||
                        PENG.PENG_PRIMERAPELLIDO || ' ' ||
                        NVL(PENG.PENG_SEGUNDOAPELLIDO, '')
                    ) AS nombreCompleto,
                    ESTP.ESTP_ID AS idEstudiantePensum,
                    PROG.PROG_ID AS idPrograma,
                    PROG.PROG_NOMBRE AS nombrePrograma,
                    ROW_NUMBER() OVER (
                        PARTITION BY PEGE.PEGE_ID
                        ORDER BY ESTP.ESTP_ID DESC NULLS LAST
                    ) AS rn
                FROM GENERAL.PERSONAGENERAL PEGE
                INNER JOIN GENERAL.PERSONANATURALGENERAL PENG
                    ON PENG.PEGE_ID = PEGE.PEGE_ID
                LEFT JOIN ACADEMICO.ESTUDIANTEPENSUM ESTP
                    ON ESTP.PEGE_ID = PEGE.PEGE_ID
                LEFT JOIN ACADEMICO.UNIDADPROGRAMA UNPR
                    ON UNPR.UNPR_ID = ESTP.UNPR_ID
                LEFT JOIN ACADEMICO.PROGRAMA PROG
                    ON PROG.PROG_ID = UNPR.PROG_ID
                WHERE
                    (:term IS NULL
                     OR UPPER(PEGE.PEGE_DOCUMENTOIDENTIDAD) LIKE UPPER('%' || :term || '%')
                     OR UPPER(PENG.PENG_PRIMERNOMBRE) LIKE UPPER('%' || :term || '%')
                     OR UPPER(PENG.PENG_SEGUNDONOMBRE) LIKE UPPER('%' || :term || '%')
                     OR UPPER(PENG.PENG_PRIMERAPELLIDO) LIKE UPPER('%' || :term || '%')
                     OR UPPER(PENG.PENG_SEGUNDOAPELLIDO) LIKE UPPER('%' || :term || '%')
                     OR UPPER(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_PRIMERAPELLIDO) LIKE UPPER('%' || :term || '%')
                     OR UPPER(PENG.PENG_PRIMERNOMBRE || ' ' || NVL(PENG.PENG_SEGUNDONOMBRE, '') || ' ' || PENG.PENG_PRIMERAPELLIDO || ' ' || NVL(PENG.PENG_SEGUNDOAPELLIDO, '')) LIKE UPPER('%' || :term || '%')
                    )
            ) T
            WHERE T.rn = 1
              AND ROWNUM <= 30
            ORDER BY T.nombreCompleto
            """, nativeQuery = true)
    List<EstudianteBusquedaProjection> searchStudents(@Param("term") String term);
}
