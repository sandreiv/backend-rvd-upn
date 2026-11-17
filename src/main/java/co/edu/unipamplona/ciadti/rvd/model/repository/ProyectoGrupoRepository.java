/**
 * Aplicación: rvd
 * Archivo: ProyectoGrupoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ProyectoGrupoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProyectoGrupoListaProjection;

public interface ProyectoGrupoRepository extends JpaRepository<ProyectoGrupoEntity, Long> {

    @Query(value = """
            SELECT
                PG.PRGR_ID AS id,
                PG.PRGR_NOMBRE AS nombre,
                PG.PRGR_DESCRIPCION AS descripcion,
                PG.PRGR_ESTADO AS estado,
                (SELECT COUNT(1) FROM RVDIES.PROYECTOS P WHERE P.PRGR_ID = PG.PRGR_ID) AS totalProyectos,
                (SELECT COUNT(1) FROM RVDIES.GRUPOCONVOCATORIA GC WHERE GC.PRGR_ID = PG.PRGR_ID AND GC.GRCO_ESTADO = '1') AS totalConvocatorias
            FROM RVDIES.PROYECTOGRUPO PG
            ORDER BY PG.PRGR_ID DESC
            """, nativeQuery = true)
    List<ProyectoGrupoListaProjection> findAllProjectGroupsList();

    @Query("SELECT pg FROM ProyectoGrupoEntity pg WHERE pg.estado = '1' ORDER BY pg.nombre ASC")
    List<ProyectoGrupoEntity> findAllActiveOrderByNombre();

    boolean existsByNombreIgnoreCase(String nombre);

    @Query("SELECT CASE WHEN COUNT(pg) > 0 THEN true ELSE false END FROM ProyectoGrupoEntity pg WHERE LOWER(pg.nombre) = LOWER(:nombre) AND pg.id <> :id")
    boolean existsByNombreIgnoreCaseAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);
}
