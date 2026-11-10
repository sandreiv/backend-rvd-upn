/**
 * Aplicación: rvd
 * Archivo: TipoActividadesRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 * 10/11/2026 - Leonel Antonio Perez Rios - Ajuste de esquema a RVDIES y remoción de columnas inexistentes
 * 10/11/2026 - Leonel Antonio Perez Rios - Adición de consulta para árbol completo y soporte de idPadre
 * 10/11/2026 - Leonel Antonio Perez Rios - Depuración de columnas no existentes (codigo, ptd) y ubicación de orden
 * 10/11/2026 - Leonel Antonio Perez Rios - Adición de idDependencia, docenciaDirecta y horasPorSemestre en proyecciones
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.query.Procedure;

import co.edu.unipamplona.ciadti.rvd.model.entity.TipoActividadesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadAdministracionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadProyectoProjection;

public interface TipoActividadesRepository
        extends JpaRepository<TipoActividadesEntity, Long> {

    @Query(value = """
            SELECT
                TIAC.TIAC_ID AS id,
                TIAC.TIAC_IDPADRE AS idPadre,
                TIAC.TIAC_NOMBRE AS nombre,
                PADRE.TIAC_NOMBRE AS nombrePadre,
                TIAC.TIAC_DESCRIPCION AS descripcion,
                TIAC.TIAC_COMPONENTE AS componente,
                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM RVDIES.TIPOACTIVIDADES HIJO
                        WHERE HIJO.TIAC_IDPADRE = TIAC.TIAC_ID
                    ) THEN 0
                    ELSE 1
                END AS esSeleccionable
            FROM RVDIES.TIPOACTIVIDADES TIAC
            LEFT JOIN RVDIES.TIPOACTIVIDADES PADRE
                ON PADRE.TIAC_ID = TIAC.TIAC_IDPADRE
            WHERE TIAC.TIAC_ESTADO = '1'
            ORDER BY
                NVL(TIAC.TIAC_IDPADRE, TIAC.TIAC_ID),
                TIAC.TIAC_IDPADRE NULLS FIRST,
                TIAC.TIAC_ORDEN,
                TIAC.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadProyectoProjection> findProjectActivityOptions();

    @Query(value = """
            SELECT
                tiac.TIAC_ID AS id,
                tiac.TIAC_IDPADRE AS idPadre,
                tiac.TIAC_NOMBRE AS nombre,
                tiac.TIAC_DESCRIPCION AS descripcion,
                tiac.TIAC_ORDEN AS orden,
                NULL AS codigo,
                tiac.TIAC_COMPONENTE AS componente
            FROM RVDIES.TIPOACTIVIDADES tiac
            WHERE tiac.TIAC_IDPADRE IS NULL
            ORDER BY tiac.TIAC_ORDEN, tiac.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadListadoProjection> findParentActivityTypes();

    @Query(value = """
            SELECT
                tiac.TIAC_ID AS id,
                tiac.TIAC_IDPADRE AS idPadre,
                tiac.TIAC_NOMBRE AS nombre,
                tiac.TIAC_DESCRIPCION AS descripcion,
                tiac.TIAC_ORDEN AS orden,
                NULL AS codigo,
                tiac.TIAC_COMPONENTE AS componente
            FROM RVDIES.TIPOACTIVIDADES tiac
            WHERE tiac.TIAC_IDPADRE IS NULL
                AND (
                    tiac.TIAC_COMPONENTE IN ('direct', 'project')
                    OR tiac.TIAC_COMPONENTE IS NULL
                )
            ORDER BY CAST(tiac.TIAC_ORDEN AS INTEGER), tiac.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadListadoProjection> findPtdActivityTypes();

    @Query(value = """
            SELECT
                tiac.TIAC_ID AS id,
                tiac.TIAC_IDPADRE AS idPadre,
                tiac.TIAC_NOMBRE AS nombre,
                tiac.TIAC_DESCRIPCION AS descripcion,
                tiac.TIAC_ORDEN AS orden,
                NULL AS codigo,
                tiac.TIAC_COMPONENTE AS componente
            FROM RVDIES.TIPOACTIVIDADES tiac
            WHERE tiac.TIAC_IDPADRE = :idPadre
            ORDER BY tiac.TIAC_ORDEN, tiac.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadListadoProjection> findByIdPadre(
            @Param("idPadre") Long idPadre);

    @Query(value = """
            SELECT
                tiac.TIAC_ID,
                tiac.TIAC_IDPADRE,
                tiac.TIAC_NOMBRE,
                tiac.TIAC_DESCRIPCION,
                tiac.TIAC_ORDEN,
                tiac.TIAC_ESTADO,
                tiac.TIAC_COMPONENTE,
                tiac.TIAC_MINIMOHORAS,
                tiac.TIAC_MAXIMOHORAS,
                tiac.TIAC_REGISTRADOPOR,
                tiac.TIAC_FECHACAMBIO
            FROM RVDIES.TIPOACTIVIDADES tiac
            WHERE tiac.TIAC_ID IN (
                SELECT descendant.TIAC_ID
                FROM RVDIES.TIPOACTIVIDADES descendant
                START WITH descendant.TIAC_ID = :idTipoActividad
                CONNECT BY PRIOR descendant.TIAC_ID = descendant.TIAC_IDPADRE
            )
            AND NOT EXISTS (
                SELECT 1
                FROM RVDIES.TIPOACTIVIDADES hijo
                WHERE hijo.TIAC_IDPADRE = tiac.TIAC_ID
            )
            ORDER BY tiac.TIAC_ORDEN, tiac.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadesEntity> findCriteriaByParentId(
            @Param("idTipoActividad") Long idTipoActividad);

    @Query(value = """
            SELECT
                TIAC.TIAC_ID AS id,
                TIAC.TIAC_IDPADRE AS idPadre,
                TIAC.TIAC_IDDEPENDENCIA AS idDependencia,
                TIAC.TIAC_ORDEN AS orden,
                TIAC.TIAC_NOMBRE AS nombre,
                TIAC.TIAC_DESCRIPCION AS descripcion,
                TIAC.TIAC_COMPONENTE AS componente,
                TIAC.TIAC_MINIMOHORAS AS minimoHoras,
                TIAC.TIAC_MAXIMOHORAS AS maximoHoras,
                TIAC.TIAC_DOCENCIADIRECTA AS docenciaDirecta,
                TIAC.TIAC_HORASPORSEMESTRE AS horasPorSemestre,
                TIAC.TIAC_ESTADO AS estado
            FROM RVDIES.TIPOACTIVIDADES TIAC
            WHERE TIAC.TIAC_IDPADRE IS NULL
            ORDER BY
                CASE
                    WHEN REGEXP_LIKE(NVL(TIAC.TIAC_ORDEN, '0'), '^[0-9]+$')
                    THEN TO_NUMBER(TIAC.TIAC_ORDEN)
                    ELSE 999999
                END,
                TIAC.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadAdministracionListadoProjection> findAdministrationParentList();

    @Query(value = """
            SELECT
                TIAC.TIAC_ID AS id,
                TIAC.TIAC_IDPADRE AS idPadre,
                TIAC.TIAC_IDDEPENDENCIA AS idDependencia,
                TIAC.TIAC_ORDEN AS orden,
                TIAC.TIAC_NOMBRE AS nombre,
                TIAC.TIAC_DESCRIPCION AS descripcion,
                TIAC.TIAC_COMPONENTE AS componente,
                TIAC.TIAC_MINIMOHORAS AS minimoHoras,
                TIAC.TIAC_MAXIMOHORAS AS maximoHoras,
                TIAC.TIAC_DOCENCIADIRECTA AS docenciaDirecta,
                TIAC.TIAC_HORASPORSEMESTRE AS horasPorSemestre,
                TIAC.TIAC_ESTADO AS estado
            FROM RVDIES.TIPOACTIVIDADES TIAC
            ORDER BY
                NVL(TIAC.TIAC_IDPADRE, TIAC.TIAC_ID),
                TIAC.TIAC_IDPADRE NULLS FIRST,
                CASE
                    WHEN REGEXP_LIKE(NVL(TIAC.TIAC_ORDEN, '0'), '^[0-9]+$')
                    THEN TO_NUMBER(TIAC.TIAC_ORDEN)
                    ELSE 999999
                END,
                TIAC.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadAdministracionListadoProjection> findAdministrationAllList();

    @Query(value = """
            SELECT
                TIAC.TIAC_ID AS id,
                TIAC.TIAC_IDPADRE AS idPadre,
                TIAC.TIAC_IDDEPENDENCIA AS idDependencia,
                TIAC.TIAC_ORDEN AS orden,
                TIAC.TIAC_NOMBRE AS nombre,
                TIAC.TIAC_DESCRIPCION AS descripcion,
                TIAC.TIAC_COMPONENTE AS componente,
                TIAC.TIAC_MINIMOHORAS AS minimoHoras,
                TIAC.TIAC_MAXIMOHORAS AS maximoHoras,
                TIAC.TIAC_DOCENCIADIRECTA AS docenciaDirecta,
                TIAC.TIAC_HORASPORSEMESTRE AS horasPorSemestre,
                TIAC.TIAC_ESTADO AS estado
            FROM RVDIES.TIPOACTIVIDADES TIAC
            WHERE TIAC.TIAC_IDPADRE IS NULL
            ORDER BY
                CASE
                    WHEN REGEXP_LIKE(NVL(TIAC.TIAC_ORDEN, '0'), '^[0-9]+$')
                    THEN TO_NUMBER(TIAC.TIAC_ORDEN)
                    ELSE 999999
                END,
                TIAC.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadAdministracionListadoProjection> findLoadRestrictionParentList();

    @Query(value = """
            SELECT
                TIAC.TIAC_ID AS id,
                TIAC.TIAC_IDPADRE AS idPadre,
                TIAC.TIAC_IDDEPENDENCIA AS idDependencia,
                TIAC.TIAC_ORDEN AS orden,
                TIAC.TIAC_NOMBRE AS nombre,
                TIAC.TIAC_DESCRIPCION AS descripcion,
                TIAC.TIAC_COMPONENTE AS componente,
                TIAC.TIAC_MINIMOHORAS AS minimoHoras,
                TIAC.TIAC_MAXIMOHORAS AS maximoHoras,
                TIAC.TIAC_DOCENCIADIRECTA AS docenciaDirecta,
                TIAC.TIAC_HORASPORSEMESTRE AS horasPorSemestre,
                TIAC.TIAC_ESTADO AS estado
            FROM RVDIES.TIPOACTIVIDADES TIAC
            WHERE TIAC.TIAC_IDPADRE = :idPadre
            ORDER BY
                CASE
                    WHEN REGEXP_LIKE(NVL(TIAC.TIAC_ORDEN, '0'), '^[0-9]+$')
                    THEN TO_NUMBER(TIAC.TIAC_ORDEN)
                    ELSE 999999
                END,
                TIAC.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadAdministracionListadoProjection> findAdministrationChildrenList(
            @Param("idPadre") Long idPadre);

    @Query(value = """
            SELECT TO_CHAR(
                COALESCE(
                    MAX(
                        CASE
                            WHEN REGEXP_LIKE(NVL(TIAC.TIAC_ORDEN, '0'), '^[0-9]+$')
                            THEN TO_NUMBER(TIAC.TIAC_ORDEN)
                            ELSE 0
                        END
                    ),
                    0
                ) + 1
            )
            FROM RVDIES.TIPOACTIVIDADES TIAC
            WHERE (
                (:idPadre IS NULL AND TIAC.TIAC_IDPADRE IS NULL)
                OR
                (:idPadre IS NOT NULL AND TIAC.TIAC_IDPADRE = :idPadre)
            )
            """, nativeQuery = true)
    String findNextOrderByParent(@Param("idPadre") Long idPadre);

    boolean existsByIdPadre(Long idPadre);

    @Procedure(name = "TipoActividadesEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_TIAC_ID") Long id,
            @Param("P_TIAC_REGISTRADOPOR") String registradoPor
    );    
}
