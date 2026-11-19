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
 * 17/11/2026 - Proyectos de la persona por PEGE_ID.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaProyectoCargaProjection;
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
                pepr.PEPR_ID AS id,
                pepr.PEGE_ID AS idPersonaGeneral,
                pepr.PROG_ID AS idPrograma,
                pepr.CECO_ID AS idCentroCosto,
                pepr.PEPR_HORAS AS horas,
                pepr.PEPR_OBSERVACION AS observacion,
                pepr.PEPR_DATA AS data,
                proy.PROY_ID AS idProyecto,
                proy.PROY_IDPROYECTO AS idProyectoPadre,
                proy.TIAC_ID AS idTipoActividad,
                tiac.TIAC_IDPADRE AS idTipoActividadPadre,
                tiac.TIAC_NOMBRE AS nombreTipoActividad,
                tiac.TIAC_DESCRIPCION AS descripcionTipoActividad,
                tiac.TIAC_ORDEN AS ordenTipoActividad,
                tiac.TIAC_ESTADO AS estadoTipoActividad,
                tiac.TIAC_MINIMOHORAS AS minimoHorasTipoActividad,
                tiac.TIAC_MAXIMOHORAS AS maximoHorasTipoActividad,
                tiac.TIAC_IDDEPENDENCIA AS idDependenciaTipoActividad,
                tiac.TIAC_DOCENCIADIRECTA AS docenciaDirectaTipoActividad,
                tiac.TIAC_COMPONENTE AS componenteTipoActividad,
                tiac.TIAC_HORASPORSEMESTRE AS horasPorSemestreTipoActividad,
                tiac.TIAC_APLICAPROYECTOS AS aplicaProyectosTipoActividad,
                proy.COOR_ID AS idCoordinacion,
                mapa.MAPA_ID AS idMateriaParticular,
                TO_CHAR(mapa.MATE_CODIGOMATERIA) AS codigoMateriaParticular,
                mate_mapa.MATE_NOMBRE AS nombreMateriaParticular,
                mate_mapa.MATE_HORASPRACTICAS AS horasPracticasMateriaParticular,
                mate_mapa.MATE_HORASTEORICAS AS horasTeoricasMateriaParticular,
                mate_mapa.MATE_HORASTEORICOPRACTICAS AS horasTeoricoPracticasMateriaParticular,
                mate_mapa.MATE_PONDERACIONACADEMICA AS ponderacionMateriaParticular,
                mate_mapa.MATE_CAPACIDAD AS capacidadMateriaParticular,
                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM ACADEMICO.GRUPO grup_mapa
                        WHERE grup_mapa.MATE_CODIGOMATERIA = mate_mapa.MATE_CODIGOMATERIA
                            AND grup_mapa.GRUP_ACTIVO = '1'
                    ) THEN 1
                    ELSE 0
                END AS tieneGrupoMateriaParticular,
                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM ACADEMICO.BANCODEMATERIAS bama
                        WHERE bama.BAMA_CODIGOMATERIA = mate_mapa.MATE_CODIGOMATERIA
                    ) THEN 1
                    ELSE 0
                END AS esElectivaMateriaParticular,
                mapa.MAPA_TIPOMATERIA AS tipoMateriaParticular,
                mapa.MAPA_DOCENTESXGRUPO AS docentesPorGrupoMateriaParticular,
                prog_mapa.PROG_ID AS idProgramaMateriaParticular,
                prog_mapa.PROG_NOMBRE AS nombreProgramaMateriaParticular,
                ceco_mapa.CECO_ID AS idCentroCostoProgramaMateriaParticular,
                ceco_mapa.CECO_DESCRIPCION AS descripcionCentroCostoProgramaMateriaParticular,
                proy.PRGR_ID AS idProyectoGrupo,
                proy.PROY_NOMBRE AS nombre,
                proy.PROY_DESCRIPCION AS descripcion,
                proy.PROY_ACTACONSEJO AS actaConsejo,
                proy.PROY_NUMEROACTA AS numeroActa,
                proy.PROY_FECHAACTA AS fechaActa,
                proy.PROY_CODIGO AS codigo,
                proy.PROY_ACTIVO AS activo
            FROM RVDIES.PERSONAPROYECTO pepr
            INNER JOIN RVDIES.PROYECTOS proy
                ON proy.PROY_ID = pepr.PROY_ID
            LEFT JOIN RVDIES.TIPOACTIVIDADES tiac
                ON tiac.TIAC_ID = proy.TIAC_ID
            LEFT JOIN RVDIES.MATERIAPARTICULAR mapa
                ON mapa.MAPA_ID = proy.MAPA_ID
            LEFT JOIN ACADEMICO.MATERIA mate_mapa
                ON mate_mapa.MATE_CODIGOMATERIA = TO_CHAR(mapa.MATE_CODIGOMATERIA)
            LEFT JOIN ACADEMICO.PROGRAMA prog_mapa
                ON prog_mapa.PROG_ID = mapa.PROG_ID
            LEFT JOIN RVDIES.PROGRAMACOORDINACION prco_mapa
                ON prco_mapa.PROG_ID = mapa.PROG_ID
                AND prco_mapa.COOR_ID = proy.COOR_ID
                AND NVL(prco_mapa.PRCO_ESTADO, '1') = '1'
            LEFT JOIN CONTABLEV3.CENTROCOSTO ceco_mapa
                ON ceco_mapa.CECO_ID = prco_mapa.CECO_ID
            WHERE pepr.PEGE_ID = :idPersonaGeneral
            ORDER BY proy.PROY_NOMBRE, pepr.PEPR_ID
            """, nativeQuery = true)
    List<PersonaProyectoCargaProjection> findCargaByIdPersonaGeneral(
            @Param("idPersonaGeneral") Long idPersonaGeneral);

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
                PEPR.CECO_ID AS idCentroCosto,
                CECO.CECO_DESCRIPCION AS centroCostoNombre,
                CECO.CECO_CODIGO AS centroCostoCodigo,
                PEPR.PROG_ID AS idPrograma,
                TO_CHAR(PEPR.PEPR_DATA) AS data,
                '1' AS estado,
                '1' AS esActivo
            FROM RVDIES.PERSONAPROYECTO PEPR
            LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = PEPR.PEGE_ID
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEPR.PEGE_ID
            LEFT JOIN CONTABLEV3.CENTROCOSTO CECO
                ON CECO.CECO_ID = PEPR.CECO_ID
            WHERE PEPR.PROY_ID = :idProyecto
            ORDER BY nombreCompleto, PEPR.PEPR_ID
            """, nativeQuery = true)
    List<PersonaProyectoListaProjection> findByIdProyecto(
            @Param("idProyecto") Long idProyecto);

    boolean existsByIdAndIdProyecto(Long id, Long idProyecto);

    boolean existsByIdProyecto(Long idProyecto);
}
