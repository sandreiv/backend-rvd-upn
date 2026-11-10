/**
 * Aplicación: rvd
 * Archivo: ProyectosRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 09/11/2026 - Leonel Antonio Perez Rios - Adaptación de consultas para gestión de proyectos sin campos de estado inexistentes
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ProyectosEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionOpcionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProyectosListaProjection;

public interface ProyectosRepository extends JpaRepository<ProyectosEntity, Long> {

    @Query(value = """
            SELECT
                PROY.PROY_ID AS id,
                PROY.TIAC_ID AS idTipoActividad,
                TIAC.TIAC_NOMBRE AS nombreTipoActividad,
                PROY.COOR_ID AS idCoordinacion,
                COALESCE(UNID_AREA.UNID_NOMBRE, 'Coordinación ' || PROY.COOR_ID) AS nombreCoordinacion,
                PROY.PROY_IDPROYECTO AS idProyectoPadre,
                PROY.MTDG_ID AS idMateriaTdg,
                PROY.PROY_NOMBRE AS nombre,
                PROY.PROY_DESCRIPCION AS descripcion,
                PROY.PROY_ACTACONSEJO AS actaConsejo,
                (SELECT COUNT(1) FROM RVDIES.PERSONAPROYECTO PEPR WHERE PEPR.PROY_ID = PROY.PROY_ID) AS totalDocentes
            FROM RVDIES.PROYECTOS PROY
            LEFT JOIN RVDIES.TIPOACTIVIDADES TIAC
                ON TIAC.TIAC_ID = PROY.TIAC_ID
            LEFT JOIN RVDIES.COORDINACIONES COOR
                ON COOR.COOR_ID = PROY.COOR_ID
            LEFT JOIN ACADEMICO.UNIDAD UNID_AREA
                ON UNID_AREA.UNID_ID = COOR.UNID_IDAREA
            WHERE PROY.PROY_IDPROYECTO IS NULL
            ORDER BY PROY.PROY_NOMBRE
            """, nativeQuery = true)
    List<ProyectosListaProjection> findParentProjectsList();

    @Query(value = """
            SELECT
                PROY.PROY_ID AS id,
                PROY.TIAC_ID AS idTipoActividad,
                TIAC.TIAC_NOMBRE AS nombreTipoActividad,
                PROY.COOR_ID AS idCoordinacion,
                COALESCE(UNID_AREA.UNID_NOMBRE, 'Coordinación ' || PROY.COOR_ID) AS nombreCoordinacion,
                PROY.PROY_IDPROYECTO AS idProyectoPadre,
                PROY.MTDG_ID AS idMateriaTdg,
                PROY.PROY_NOMBRE AS nombre,
                PROY.PROY_DESCRIPCION AS descripcion,
                PROY.PROY_ACTACONSEJO AS actaConsejo,
                (SELECT COUNT(1) FROM RVDIES.PERSONAPROYECTO PEPR WHERE PEPR.PROY_ID = PROY.PROY_ID) AS totalDocentes
            FROM RVDIES.PROYECTOS PROY
            LEFT JOIN RVDIES.TIPOACTIVIDADES TIAC
                ON TIAC.TIAC_ID = PROY.TIAC_ID
            LEFT JOIN RVDIES.COORDINACIONES COOR
                ON COOR.COOR_ID = PROY.COOR_ID
            LEFT JOIN ACADEMICO.UNIDAD UNID_AREA
                ON UNID_AREA.UNID_ID = COOR.UNID_IDAREA
            WHERE PROY.PROY_IDPROYECTO = :idProyecto
            ORDER BY PROY.PROY_NOMBRE
            """, nativeQuery = true)
    List<ProyectosListaProjection> findProductsByParentProjectId(@Param("idProyecto") Long idProyecto);

    boolean existsByIdProyectoPadre(Long idProyectoPadre);

    @Query(value = """
            SELECT
                COOR.COOR_ID AS id,
                COALESCE(UNID_AREA.UNID_NOMBRE, 'Coordinación ' || COOR.COOR_ID) AS nombre,
                COOR.COOR_ESACADEMICA AS descripcion
            FROM RVDIES.COORDINACIONES COOR
            LEFT JOIN ACADEMICO.UNIDAD UNID_AREA
                ON UNID_AREA.UNID_ID = COOR.UNID_IDAREA
            WHERE (
                :term IS NULL
                OR LOWER(UNID_AREA.UNID_NOMBRE) LIKE '%' || LOWER(:term) || '%'
                OR TO_CHAR(COOR.COOR_ID) = :term
            )
            ORDER BY UNID_AREA.UNID_NOMBRE ASC
            FETCH FIRST 30 ROWS ONLY
            """, nativeQuery = true)
    List<CoordinacionOpcionProjection> searchCoordinations(@Param("term") String term);
}
