/**
 * Aplicación: rvd
 * Archivo: VinculacionActividadRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Juan Diego Florez Vera - Creación inicial para administración de tablas básicas
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.VinculacionActividadesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.IdNombreGrupoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.VinculacionActividadProjection;

public interface VinculacionActividadRepository
        extends JpaRepository<VinculacionActividadesEntity, Long> {

    /**
     * Listado con los nombres de los dos catálogos asociados.
     * RVDIES.VINCULACIONACTIVIDADES no tiene FK declarada: la relación con
     * TIPOVINCULACION y TIPOACTIVIDADES es lógica.
     */
    @Query(value = """
            SELECT
                VIAC.VIAC_ID AS id,
                VIAC.TIVI_ID AS idTipoVinculacion,
                CL.CLDO_DESCRIPCION || ' - ' || DE.DEDO_DESCRIPCION
                    AS tipoVinculacion,
                VIAC.TIAC_ID AS idTipoActividad,
                TIAC.TIAC_NOMBRE AS tipoActividad,
                VIAC.VIAC_ESTADO AS estado,
                VIAC.VIAC_MINIMOHORAS AS minimoHoras,
                VIAC.VIAC_MAXIMOHORAS AS maximoHoras
            FROM RVDIES.VINCULACIONACTIVIDADES VIAC
            INNER JOIN RVDIES.TIPOVINCULACION TIVI
                ON TIVI.TIVI_ID = VIAC.TIVI_ID
            INNER JOIN ACADEMICO.CLASIFICACIONDOCENTE CL
                ON CL.CLDO_ID = TIVI.CLDO_ID
            INNER JOIN ACADEMICO.DEDICACIONDOCENTE DE
                ON DE.DEDO_ID = TIVI.DEDO_ID
            INNER JOIN RVDIES.TIPOACTIVIDADES TIAC
                ON TIAC.TIAC_ID = VIAC.TIAC_ID
            ORDER BY
                CL.CLDO_DESCRIPCION,
                DE.DEDO_DESCRIPCION,
                TIAC.TIAC_ORDEN,
                TIAC.TIAC_NOMBRE
            """, nativeQuery = true)
    List<VinculacionActividadProjection> findAllVinculacionActividades();

    @Query(value = """
            SELECT
                COUNT(*)
            FROM RVDIES.VINCULACIONACTIVIDADES VIAC
            WHERE VIAC.TIVI_ID = :idTipoVinculacion
                AND VIAC.TIAC_ID = :idTipoActividad
            """, nativeQuery = true)
    long countByVinculacionAndActividad(
            @Param("idTipoVinculacion") Long idTipoVinculacion,
            @Param("idTipoActividad") Long idTipoActividad
    );

    @Query(value = """
            SELECT
                COUNT(*)
            FROM RVDIES.VINCULACIONACTIVIDADES VIAC
            WHERE VIAC.TIVI_ID = :idTipoVinculacion
                AND VIAC.TIAC_ID = :idTipoActividad
                AND VIAC.VIAC_ID <> :id
            """, nativeQuery = true)
    long countByVinculacionAndActividadAndIdNot(
            @Param("idTipoVinculacion") Long idTipoVinculacion,
            @Param("idTipoActividad") Long idTipoActividad,
            @Param("id") Long id
    );

    @Query(value = """
            SELECT
                COUNT(*)
            FROM RVDIES.TIPOVINCULACION TIVI
            WHERE TIVI.TIVI_ID = :id
            """, nativeQuery = true)
    long countTipoVinculacion(
            @Param("id") Long id
    );

    @Query(value = """
            SELECT
                COUNT(*)
            FROM RVDIES.TIPOACTIVIDADES TIAC
            WHERE TIAC.TIAC_ID = :id
            """, nativeQuery = true)
    long countTipoActividad(
            @Param("id") Long id
    );

    @Query(value = """
            SELECT
                TIVI.TIVI_ID AS id,
                CL.CLDO_DESCRIPCION || ' - ' || DE.DEDO_DESCRIPCION AS nombre,
                CAST(NULL AS VARCHAR2(100)) AS grupo
            FROM RVDIES.TIPOVINCULACION TIVI
            INNER JOIN ACADEMICO.CLASIFICACIONDOCENTE CL
                ON CL.CLDO_ID = TIVI.CLDO_ID
            INNER JOIN ACADEMICO.DEDICACIONDOCENTE DE
                ON DE.DEDO_ID = TIVI.DEDO_ID
            ORDER BY
                CL.CLDO_DESCRIPCION,
                DE.DEDO_DESCRIPCION
            """, nativeQuery = true)
    List<IdNombreGrupoProjection> findTiposVinculacion();

    /**
     * Actividades seleccionables: activas y sin hijas, agrupadas por su padre.
     */
    @Query(value = """
            SELECT
                TIAC.TIAC_ID AS id,
                TIAC.TIAC_NOMBRE AS nombre,
                PADRE.TIAC_NOMBRE AS grupo
            FROM RVDIES.TIPOACTIVIDADES TIAC
            LEFT JOIN RVDIES.TIPOACTIVIDADES PADRE
                ON PADRE.TIAC_ID = TIAC.TIAC_IDPADRE
            WHERE TIAC.TIAC_ESTADO = '1'
                AND NOT EXISTS (
                    SELECT 1
                    FROM RVDIES.TIPOACTIVIDADES HIJO
                    WHERE HIJO.TIAC_IDPADRE = TIAC.TIAC_ID
                )
            ORDER BY
                NVL(PADRE.TIAC_NOMBRE, TIAC.TIAC_NOMBRE),
                TIAC.TIAC_ORDEN,
                TIAC.TIAC_NOMBRE
            """, nativeQuery = true)
    List<IdNombreGrupoProjection> findTiposActividadSeleccionables();
}
