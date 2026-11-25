/**
 * Aplicación: rvd
 * Archivo: NovedadRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 15/10/2026 - Andrés Hernández - Buscar novedades para los modalidad Planta
 * 24/11/2026 - Consultas de la administración de tablas básicas sobre
 *              RVDIES.NOVEDADES (duplicado y usos); se retiran las consultas
 *              por acción y planta porque esas columnas no existen en RVDIES
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;

public interface NovedadRepository
        extends JpaRepository<NovedadEntity, Long> {

    @Query(value = """
            SELECT
                NOVE.*
            FROM RVDIES.NOVEDADES NOVE
            ORDER BY NOVE.NOVE_TIPO
            """, nativeQuery = true)
    List<NovedadEntity> findAllNovedades();

    @Query(value = """
            SELECT
                COUNT(*)
            FROM RVDIES.NOVEDADES NOVE
            WHERE UPPER(TRIM(NOVE.NOVE_TIPO)) =
                UPPER(TRIM(:tipo))
            """, nativeQuery = true)
    long countByTipo(
            @Param("tipo") String tipo
    );

    @Query(value = """
            SELECT
                COUNT(*)
            FROM RVDIES.NOVEDADES NOVE
            WHERE UPPER(TRIM(NOVE.NOVE_TIPO)) =
                UPPER(TRIM(:tipo))
                AND NOVE.NOVE_ID <> :id
            """, nativeQuery = true)
    long countByTipoAndIdNot(
            @Param("tipo") String tipo,
            @Param("id") Long id
    );

    /**
     * Cuenta los usos de la novedad en las cargas docentes. La tabla no tiene
     * FK declarada, así que sin esta validación el borrado dejaría cargas
     * apuntando a un id inexistente.
     */
    @Query(value = """
            SELECT
                COUNT(*)
            FROM RVDIES.CARGADOCENTE CADO
            WHERE CADO.NOVE_ID = :id
            """, nativeQuery = true)
    long countUsosNovedad(
            @Param("id") Long id
    );
}
