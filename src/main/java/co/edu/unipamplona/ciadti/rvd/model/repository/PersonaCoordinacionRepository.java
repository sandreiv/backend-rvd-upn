/**
 * Aplicación: rvd
 * Archivo: PersonaCoordinacionRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionPersonaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaCoordinacionListadoProjection;

public interface PersonaCoordinacionRepository extends JpaRepository<PersonaCoordinacionEntity, Long> {

    @Query(value = """
            SELECT
                PECO.PEGE_ID AS idPersonaGeneral,
                COALESCE(
                    NULLIF(TRIM(
                        NVL(PENG.PENG_PRIMERAPELLIDO, '') || ' ' ||
                        NVL(PENG.PENG_SEGUNDOAPELLIDO, '') || ' ' ||
                        NVL(PENG.PENG_PRIMERNOMBRE, '') || ' ' ||
                        NVL(PENG.PENG_SEGUNDONOMBRE, '')
                    ), ''),
                    'Persona ' || PECO.PEGE_ID
                ) AS persona,
                PEGE.PEGE_DOCUMENTOIDENTIDAD AS documentoIdentidad,
                COOR.COOR_ID AS idCoordinacion,
                COALESCE(COOR.COOR_NOMBRE, COOR.COOR_DESCRIPCION) AS coordinacion,
                TO_CHAR(PECO.PECO_ESTADO) AS estado
            FROM RVD.PERSONACOORDINACION PECO
            LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = PECO.PEGE_ID
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PECO.PEGE_ID
            LEFT JOIN RVD.COORDINACIONES COOR
                ON COOR.COOR_ID = PECO.COOR_ID
            ORDER BY PECO.PEGE_ID, COOR.COOR_ID
            """, nativeQuery = true)
    List<PersonaCoordinacionListadoProjection> findAdministrationList();

    @Query(value = """
            SELECT
                COOR.COOR_ID AS id,
                UNID_REG.UNID_ID AS idUnidadRegional,
                UNID_REG.UNID_NOMBRE AS nombreUnidadRegional,
                UNID_AREA.UNID_ID AS idUnidadArea,
                UNID_AREA.UNID_NOMBRE AS nombreUnidadArea,
                COOR.COOR_IDPADRE AS idCoordinacionPadre,
                UNID_AREA_PADRE.UNID_ID AS idUnidadAreaPadre,
                UNID_AREA_PADRE.UNID_NOMBRE AS nombreUnidadAreaPadre,
                COOR.COOR_ESACADEMICA AS esAcademica,
                CONV.CONV_ID AS idConvocatoria,
                CONV.CONV_NOMBRE AS nombreConvocatoria,
                CONV.CONV_DESCRIPCION AS descripcionConvocatoria,
                CONV.CONV_ESTADO AS estadoConvocatoria,
                PEUN.PEUN_ID AS idPeriodoUniversidad,
                PEUN.PEUN_ANO AS anioPeriodo,
                PEUN.PEUN_PERIODO AS periodo
            FROM RVDIES.PERSONACOORDINACION PECO
            INNER JOIN RVDIES.COORDINACIONES COOR
                ON COOR.COOR_ID = PECO.COOR_ID
            LEFT JOIN ACADEMICO.UNIDAD UNID_REG
                ON UNID_REG.UNID_ID = COOR.UNID_IDREGIONAL
            LEFT JOIN ACADEMICO.UNIDAD UNID_AREA
                ON UNID_AREA.UNID_ID = COOR.UNID_IDAREA
            LEFT JOIN RVDIES.COORDINACIONES COOR_PADRE
                ON COOR_PADRE.COOR_ID = COOR.COOR_IDPADRE
            LEFT JOIN ACADEMICO.UNIDAD UNID_AREA_PADRE
                ON UNID_AREA_PADRE.UNID_ID = COOR_PADRE.UNID_IDAREA
            LEFT JOIN RVDIES.CONVOCATORIA CONV
                ON CONV.PEUN_ID = :idPeriodoUniversidad
                AND CONV.CONV_ESTADO = '1'
                AND CONV.CONV_ID = NVL(:idConvocatoria, CONV.CONV_ID)
            LEFT JOIN ACADEMICO.PERIODOUNIVERSIDAD PEUN
                ON PEUN.PEUN_ID = CONV.PEUN_ID
            WHERE PECO.PEGE_ID = :idPersona
                AND NVL(PECO.PECO_ESTADO, '1') = '1'
            ORDER BY COOR.COOR_ID, CONV.CONV_ID
            """, nativeQuery = true)
    List<CoordinacionPersonaProjection> findActiveByPersonAndCall(
            @Param("idPersona") Long idPersona,
            @Param("idPeriodoUniversidad") Long idPeriodoUniversidad,
            @Param("idConvocatoria") Long idConvocatoria);
}

/* 17/07/2026 @:Daniel Arias */
