/**
 * Aplicación: rvd
 * Archivo: PensumRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Listado de pensum por programa.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PensumEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PensumListadoProjection;

public interface PensumRepository extends JpaRepository<PensumEntity, Long> {

    @Query(value = """
            SELECT
                PENS.PENS_ID AS id,
                PENS.PENS_DESCRIPCION AS descripcion,
                PENS.PENS_ANOINICIO AS anioInicio,
                PENS.PENS_PERIODOINICIO AS periodoInicio,
                PENS.PENS_NUMPERIODOS AS numPeriodos
            FROM ACADEMICO.PENSUM PENS
            WHERE PENS.PROG_ID = :idPrograma
            ORDER BY PENS.PENS_ANOINICIO DESC, PENS.PENS_PERIODOINICIO DESC, PENS.PENS_ID DESC
            """, nativeQuery = true)
    List<PensumListadoProjection> findByIdPrograma(@Param("idPrograma") Long idPrograma);
}
