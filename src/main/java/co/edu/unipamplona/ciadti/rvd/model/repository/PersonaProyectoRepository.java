/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 16/09/2026 - Andrés Hernández - Manejo de relación de actividad entre persona y proyecto
 * 09/11/2026 - Leonel Antonio Perez Rios - Adaptación de consultas sin campos inexistentes (TIAC_CODIGO, PEPR_ESTADO, PROY_ESTADO)
 * 10/11/2026 - Leonel Antonio Perez Rios - Remoción de PEPR_ROL inexistente en BD
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaProyectoListaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProyectoDocenteListadoProjection;

public interface PersonaProyectoRepository
        extends JpaRepository<PersonaProyectoEntity, Long> {

    @Query(value = """
            SELECT
                proy.PROY_ID AS id,
                proy.PROY_IDPROYECTO AS idProyecto,
                proy.PROY_NOMBRE AS nombre,
                proy.PROY_DESCRIPCION AS descripcion,
                proy.TIAC_ID AS idTipoProyecto,
                tiac.TIAC_NOMBRE AS nombreTipoProyecto,
                tiac.TIAC_DESCRIPCION AS descripcionTipoProyecto,
                tiac.TIAC_COMPONENTE AS tipoTipoProyecto,
                pepr.PEPR_ID AS idPersonaProyecto,
                pepr.PEGE_ID AS idPersonaGeneral,
                pepr.PEPR_HORAS AS horas,
                pepr.PEPR_OBSERVACION AS tipo,
                pepr.PEPR_OBSERVACION AS observacion,
                tiac.TIAC_ID AS idTipoActividad,
                tiac.TIAC_IDPADRE AS idPadreTipoActividad,
                tiac.TIAC_NOMBRE AS nombreTipoActividad,
                tiac.TIAC_DESCRIPCION AS descripcionTipoActividad,
                tiac.TIAC_ORDEN AS ordenTipoActividad,
                NULL AS codigoTipoActividad,
                tiac.TIAC_COMPONENTE AS componenteTipoActividad
            FROM RVDIES.PROYECTOS proy
            INNER JOIN RVDIES.PERSONAPROYECTO pepr
                ON proy.PROY_ID = pepr.PROY_ID
            INNER JOIN RVDIES.TIPOACTIVIDADES tiac
                ON proy.TIAC_ID = tiac.TIAC_ID
            WHERE pepr.PEGE_ID = :idPersonaGeneral
            ORDER BY proy.PROY_NOMBRE, pepr.PEPR_ID
            """, nativeQuery = true)
    List<ProyectoDocenteListadoProjection>
            findProyectosByIdPersonaGeneral(
                    @Param("idPersonaGeneral") Long idPersonaGeneral
            );

    @Query(value = """
            SELECT
                proy.PROY_ID AS id,
                proy.PROY_IDPROYECTO AS idProyecto,
                proy.PROY_NOMBRE AS nombre,
                proy.PROY_DESCRIPCION AS descripcion,
                proy.TIAC_ID AS idTipoProyecto,
                tiac.TIAC_NOMBRE AS nombreTipoProyecto,
                tiac.TIAC_DESCRIPCION AS descripcionTipoProyecto,
                tiac.TIAC_COMPONENTE AS tipoTipoProyecto,
                pepr.PEPR_ID AS idPersonaProyecto,
                pepr.PEGE_ID AS idPersonaGeneral,
                pepr.PEPR_HORAS AS horas,
                pepr.PEPR_OBSERVACION AS tipo,
                pepr.PEPR_OBSERVACION AS observacion,
                tiac.TIAC_ID AS idTipoActividad,
                tiac.TIAC_IDPADRE AS idPadreTipoActividad,
                tiac.TIAC_NOMBRE AS nombreTipoActividad,
                tiac.TIAC_DESCRIPCION AS descripcionTipoActividad,
                tiac.TIAC_ORDEN AS ordenTipoActividad,
                NULL AS codigoTipoActividad,
                tiac.TIAC_COMPONENTE AS componenteTipoActividad
            FROM RVDIES.PROYECTOS proy
            INNER JOIN RVDIES.PERSONAPROYECTO pepr
                ON proy.PROY_ID = pepr.PROY_ID
            INNER JOIN RVDIES.TIPOACTIVIDADES tiac
                ON proy.TIAC_ID = tiac.TIAC_ID
            WHERE pepr.PEGE_ID = :idPersonaGeneral
            ORDER BY proy.PROY_NOMBRE, pepr.PEPR_ID
            """, nativeQuery = true)
    List<ProyectoDocenteListadoProjection>
            findProyectosByIdPersonaGeneralAndConvocatoria(
                    @Param("idPersonaGeneral") Long idPersonaGeneral,
                    @Param("idConvocatoria") Long idConvocatoria
            );

    @Query(value = """
            SELECT
                PEPR.PEPR_ID AS id,
                PEPR.PROY_ID AS idProyecto,
                PEPR.PEGE_ID AS idPersonaGeneral,
                COALESCE(
                    NULLIF(TRIM(
                        NVL(PENG.PENG_PRIMERAPELLIDO, '') || ' ' ||
                        NVL(PENG.PENG_SEGUNDOAPELLIDO, '') || ' ' ||
                        NVL(PENG.PENG_PRIMERNOMBRE, '') || ' ' ||
                        NVL(PENG.PENG_SEGUNDONOMBRE, '')
                    ), ''),
                    'Persona ' || PEPR.PEGE_ID
                ) AS nombreCompleto,
                PEGE.PEGE_DOCUMENTOIDENTIDAD AS documento,
                PEPR.PEPR_HORAS AS horas,
                PEPR.PEPR_OBSERVACION AS observacion,
                '1' AS estado,
                '1' AS esActivo
            FROM RVDIES.PERSONAPROYECTO PEPR
            LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = PEPR.PEGE_ID
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEPR.PEGE_ID
            WHERE PEPR.PROY_ID = :idProyecto
            ORDER BY nombreCompleto, PEPR.PEPR_ID
            """, nativeQuery = true)
    List<PersonaProyectoListaProjection> findByIdProyecto(
            @Param("idProyecto") Long idProyecto);

    boolean existsByIdAndIdProyecto(Long id, Long idProyecto);

    boolean existsByIdProyecto(Long idProyecto);
}
