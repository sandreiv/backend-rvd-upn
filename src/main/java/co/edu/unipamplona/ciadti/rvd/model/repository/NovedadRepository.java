/**
 * Aplicación: rvd
 * Archivo: NovedadRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 15/10/2026 - Andrés Hernández - Buscar novedades para los modalidad Planta
 * 24/11/2026 - Consultas de duplicado y de usos, y eliminación por procedimiento
 *              para la administración de tablas básicas
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;

public interface NovedadRepository
        extends JpaRepository<NovedadEntity, Long> {

    @Query(value = """
            SELECT
                NOVE.*
            FROM RVD.NOVEDADES NOVE
            ORDER BY
                NOVE.NOVE_ACCION,
                NOVE.NOVE_TIPO
            """, nativeQuery = true)
    List<NovedadEntity> findAllNovedades();

    @Query(value = """
            SELECT
                NOVE.*
            FROM RVD.NOVEDADES NOVE
            WHERE UPPER(TRIM(NOVE.NOVE_ACCION)) =
                UPPER(TRIM(:accion))
            ORDER BY
                NOVE.NOVE_ACCION,
                NOVE.NOVE_TIPO
            """, nativeQuery = true)
    List<NovedadEntity> findAllNovedadesPorAccion(
            @Param("accion") String accion
    );

    @Query(value = """
            SELECT
                NOVE.*
            FROM RVD.NOVEDADES NOVE
            WHERE UPPER(TRIM(NOVE.NOVE_ACCION)) = UPPER(TRIM(:accion))
                AND NOVE.NOVE_FORPLANTA = '1'
            ORDER BY
                NOVE.NOVE_ACCION,
                NOVE.NOVE_TIPO
            """, nativeQuery = true)
    List<NovedadEntity> findAllNovedadesPlantaPorAccion(
            @Param("accion") String accion
    );

    @Query(value = """
            SELECT
                COUNT(*)
            FROM RVD.NOVEDADES NOVE
            WHERE UPPER(TRIM(NOVE.NOVE_TIPO)) =
                UPPER(TRIM(:tipo))
                AND UPPER(TRIM(NOVE.NOVE_ACCION)) =
                UPPER(TRIM(:accion))
            """, nativeQuery = true)
    long countByTipoAndAccion(
            @Param("tipo") String tipo,
            @Param("accion") String accion
    );

    @Query(value = """
            SELECT
                COUNT(*)
            FROM RVD.NOVEDADES NOVE
            WHERE UPPER(TRIM(NOVE.NOVE_TIPO)) =
                UPPER(TRIM(:tipo))
                AND UPPER(TRIM(NOVE.NOVE_ACCION)) =
                UPPER(TRIM(:accion))
                AND NOVE.NOVE_ID <> :id
            """, nativeQuery = true)
    long countByTipoAndAccionAndIdNot(
            @Param("tipo") String tipo,
            @Param("accion") String accion,
            @Param("id") Long id
    );

    /**
     * Cuenta los usos de la novedad en las tablas que la referencian.
     * RVD.NOVEDADES no tiene FK declarada, así que sin esta validación el
     * borrado dejaría cargas docentes apuntando a un id inexistente.
     */
    @Query(value = """
            SELECT
                (
                    SELECT COUNT(*)
                    FROM RVD.NOVEDADCARGADOCENTE NOCD
                    WHERE NOCD.NOVE_ID = :id
                ) + (
                    SELECT COUNT(*)
                    FROM RVDIES.CARGADOCENTE CADO
                    WHERE CADO.NOVE_ID = :id
                )
            FROM DUAL
            """, nativeQuery = true)
    long countUsosNovedad(
            @Param("id") Long id
    );

    @Procedure(
        name = "NovedadEntity.deleteByProcedure"
    )
    BigDecimal deleteByProcedure(
            @Param("P_NOVE_ID")
            Long id,

            @Param("P_NOVE_REGISTRADOPOR")
            String registradoPor
    );
}
