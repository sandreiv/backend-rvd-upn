/**
 * Aplicación: rvd
 * Archivo: RestriccionActividadesRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Creación inicial.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionActividadesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.RestriccionActividadListadoProjection;

public interface RestriccionActividadesRepository
        extends JpaRepository<RestriccionActividadesEntity, Long> {

    @Query(value = """
            SELECT
                REAC.REAC_ID AS id,
                REAC.TIAC_ID AS idTipoActividad,
                TIAC.TIAC_NOMBRE AS nombreTipoActividad
            FROM RVDIES.RESTRICCIONACTIVIDADES REAC
            LEFT JOIN RVDIES.TIPOACTIVIDADES TIAC
                ON TIAC.TIAC_ID = REAC.TIAC_ID
            WHERE REAC.TIVI_ID = :idTipoVinculacion
            ORDER BY
                CASE
                    WHEN REGEXP_LIKE(NVL(TIAC.TIAC_ORDEN, '0'), '^[0-9]+$')
                    THEN TO_NUMBER(TIAC.TIAC_ORDEN)
                    ELSE 999999
                END,
                TIAC.TIAC_NOMBRE,
                REAC.REAC_ID
            """, nativeQuery = true)
    List<RestriccionActividadListadoProjection> findByIdTipoVinculacion(
            @Param("idTipoVinculacion") Long idTipoVinculacion);
}

/* 09/11/2026 */
