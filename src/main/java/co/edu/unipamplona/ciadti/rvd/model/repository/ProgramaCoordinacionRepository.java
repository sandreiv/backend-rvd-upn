/**
 * Aplicación: rvd
 * Archivo: ProgramaCoordinacionRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ProgramaCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.AsociacionCoordinacionListadoProjection;

public interface ProgramaCoordinacionRepository extends JpaRepository<ProgramaCoordinacionEntity, Long> {

    boolean existsByIdCoordinacion(Long idCoordinacion);

    @Query(value = """
            SELECT
                PRCO.PRCO_ID AS id,
                COOR.COOR_ID AS idCoordinacion,
                UNID_AREA.UNID_NOMBRE AS coordinacion,
                PROG.PROG_ID AS idPrograma,
                PROG.PROG_NOMBRE AS programa,
                CECO.CECO_ID AS idCentroCosto,
                CASE
                    WHEN CECO.CECO_ID IS NULL THEN NULL
                    ELSE CECO.CECO_DESCRIPCION
                END AS centroCosto,
                PRCO.PRCO_ESTADO AS estado
            FROM RVDIES.PROGRAMACOORDINACION PRCO
            LEFT JOIN RVDIES.COORDINACIONES COOR
                ON COOR.COOR_ID = PRCO.COOR_ID
            LEFT JOIN ACADEMICO.UNIDAD UNID_AREA
                ON UNID_AREA.UNID_ID = COOR.UNID_IDAREA
            LEFT JOIN ACADEMICO.PROGRAMA PROG
                ON PROG.PROG_ID = PRCO.PROG_ID
            LEFT JOIN CONTABLEV3.CENTROCOSTO CECO
                ON CECO.CECO_ID = PRCO.CECO_ID
            ORDER BY UNID_AREA.UNID_NOMBRE, PROG.PROG_NOMBRE
            """, nativeQuery = true)
    List<AsociacionCoordinacionListadoProjection> findAdministrationList();

    @Query(value = """
            SELECT COUNT(*)
            FROM RVD.ASOCIACIONCOORDINACION asco
            WHERE asco.COOR_ID = :idCoordinacion
                AND asco.PROG_ID IS NOT NULL
                AND asco.MATE_CODIGOMATERIA IS NULL
            """, nativeQuery = true)
    Long countProgramasByCoordinacion(
            @Param("idCoordinacion") Long idCoordinacion);

    default boolean existsProgramasByCoordinacion(Long idCoordinacion) {
        Long count = countProgramasByCoordinacion(idCoordinacion);
        return count != null && count > 0;
    }

    @Query(value = """
            SELECT COUNT(*)
            FROM RVD.ASOCIACIONCOORDINACION asco
            WHERE asco.COOR_ID = :idCoordinacion
                AND asco.MATE_CODIGOMATERIA IS NOT NULL
                AND asco.PROG_ID IS NULL
            """, nativeQuery = true)
    Long countMateriasByCoordinacion(
            @Param("idCoordinacion") Long idCoordinacion);

    default boolean existsMateriasByCoordinacion(Long idCoordinacion) {
        Long count = countMateriasByCoordinacion(idCoordinacion);
        return count != null && count > 0;
    }

    @Query(value = """
            SELECT asco.CECO_ID
            FROM RVD.ASOCIACIONCOORDINACION asco
            WHERE asco.COOR_ID = :idCoordinacion
                AND asco.PROG_ID = :idPrograma
                AND asco.PROG_ID IS NOT NULL
                AND asco.MATE_CODIGOMATERIA IS NULL
                AND asco.CECO_ID IS NOT NULL
                AND ROWNUM = 1
            """, nativeQuery = true)
    Optional<Long> findIdCentroCostoByCoordinacionAndPrograma(
            @Param("idCoordinacion") Long idCoordinacion,
            @Param("idPrograma") Long idPrograma);

}