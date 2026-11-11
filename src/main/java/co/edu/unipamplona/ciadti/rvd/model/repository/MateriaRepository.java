package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.MateriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaPensumProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaCatalogoAdministracionProjection;

public interface MateriaRepository extends JpaRepository<MateriaEntity, String> {

    @Query(value = """
            SELECT
                mate.MATE_CODIGOMATERIA AS codigoMateria,
                mate.MATE_NOMBRE AS nombre,
                mate.MATE_HORASPRACTICAS AS horasPracticas,
                mate.MATE_HORASTEORICAS AS horasTeoricas,
                mate.MATE_HORASTEORICOPRACTICAS AS horasTeoricoPracticas,
                pema.PEMA_PERIODO AS periodo,
                mate.MATE_PONDERACIONACADEMICA AS ponderacionAcademica,
                CAST(NULL AS NUMBER) AS capacidad,
                CAST(NULL AS NUMBER) AS idCentroCosto,
                CAST(NULL AS VARCHAR2(255)) AS descripcionCentroCosto
            FROM ACADEMICO.MATERIA mate
            INNER JOIN ACADEMICO.PENSUMMATERIA pema
                ON mate.MATE_CODIGOMATERIA = pema.MATE_CODIGOMATERIA
            WHERE pema.PROG_ID = :idPrograma
            ORDER BY mate.MATE_NOMBRE, pema.PEMA_PERIODO
            """, nativeQuery = true)
    List<MateriaListadoProjection> findByPrograma(
            @Param("idPrograma") Long idPrograma);

    @Query(value = """
            SELECT
                mate.MATE_CODIGOMATERIA AS codigoMateria,
                mate.MATE_NOMBRE AS nombre,
                mate.MATE_HORASPRACTICAS AS horasPracticas,
                mate.MATE_HORASTEORICAS AS horasTeoricas,
                mate.MATE_HORASTEORICOPRACTICAS AS horasTeoricoPracticas,
                pema.PEMA_PERIODO AS periodo,
                mate.MATE_PONDERACIONACADEMICA AS ponderacionAcademica,
                CAST(NULL AS NUMBER) AS capacidad,
                CAST(NULL AS NUMBER) AS idCentroCosto,
                CAST(NULL AS VARCHAR2(255)) AS descripcionCentroCosto
            FROM ACADEMICO.MATERIA mate
            INNER JOIN ACADEMICO.PENSUMMATERIA pema
                ON mate.MATE_CODIGOMATERIA = pema.MATE_CODIGOMATERIA
            WHERE pema.PROG_ID = :idPrograma
                AND mate.MATE_CODIGOMATERIA NOT IN (
                    SELECT asco.MATE_CODIGOMATERIA
                    FROM RVD.ASOCIACIONCOORDINACION asco
                    WHERE asco.MATE_CODIGOMATERIA IS NOT NULL
                        AND asco.PROG_ID IS NULL
                )
            ORDER BY mate.MATE_NOMBRE, pema.PEMA_PERIODO
            """, nativeQuery = true)
    List<MateriaListadoProjection> findPensumExcluyendoTransversales(
            @Param("idPrograma") Long idPrograma);

    @Query(value = """
            SELECT
                mate.MATE_CODIGOMATERIA AS codigoMateria,
                mate.MATE_NOMBRE AS nombre,
                mate.MATE_HORASPRACTICAS AS horasPracticas,
                mate.MATE_HORASTEORICAS AS horasTeoricas,
                mate.MATE_HORASTEORICOPRACTICAS AS horasTeoricoPracticas,
                pema.PEMA_PERIODO AS periodo,
                mate.MATE_PONDERACIONACADEMICA AS ponderacionAcademica,
                CAST(NULL AS NUMBER) AS capacidad,
                ceco.CECO_ID AS idCentroCosto,
                ceco.CECO_DESCRIPCION AS descripcionCentroCosto
            FROM ACADEMICO.MATERIA mate
            INNER JOIN RVD.ASOCIACIONCOORDINACION asco
                ON mate.MATE_CODIGOMATERIA = asco.MATE_CODIGOMATERIA
            INNER JOIN ACADEMICO.PENSUMMATERIA pema
                ON mate.MATE_CODIGOMATERIA = pema.MATE_CODIGOMATERIA
                AND pema.PROG_ID = :idPrograma
            LEFT JOIN CONTABLEV3.CENTROCOSTO ceco
                ON asco.CECO_ID = ceco.CECO_ID
            WHERE asco.COOR_ID = :idCoordinacion
                AND asco.MATE_CODIGOMATERIA IS NOT NULL
                AND asco.PROG_ID IS NULL
            ORDER BY mate.MATE_NOMBRE, pema.PEMA_PERIODO
            """, nativeQuery = true)
    List<MateriaListadoProjection> findTransversalesByCoordinacionAndPrograma(
            @Param("idCoordinacion") Long idCoordinacion,
            @Param("idPrograma") Long idPrograma);




    @Query(value = """
            SELECT
                MATE.MATE_CODIGOMATERIA AS codigoMateria,
                MATE.MATE_NOMBRE AS label
            FROM ACADEMICO.MATERIA MATE
            WHERE MATE.MATE_NOMBRE IS NOT NULL
            ORDER BY MATE.MATE_NOMBRE
            """, nativeQuery = true)
    List<MateriaCatalogoAdministracionProjection> findAdministrationOptions();

    @Query(value = """
            SELECT
                MATE.MATE_CODIGOMATERIA AS codigoMateria,
                MATE.MATE_NOMBRE AS nombre,
                MATE.MATE_HORASPRACTICAS AS horasPracticas,
                MATE.MATE_HORASTEORICAS AS horasTeoricas,
                MATE.MATE_HORASTEORICOPRACTICAS AS horasTeoricoPracticas,
                PEMA.PEMA_PERIODO AS periodo,
                MATE.MATE_PONDERACIONACADEMICA AS ponderacionAcademica,
                MATE.MATE_CAPACIDAD AS capacidad,
                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM ACADEMICO.GRUPO GRUP
                        WHERE GRUP.MATE_CODIGOMATERIA = MATE.MATE_CODIGOMATERIA
                            AND GRUP.GRUP_ACTIVO = '1'
                    ) THEN 1
                    ELSE 0
                END AS tieneGrupo,
                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM ACADEMICO.BANCODEMATERIAS BAMA
                        WHERE BAMA.BAMA_CODIGOMATERIA = MATE.MATE_CODIGOMATERIA
                    ) THEN 1
                    ELSE 0
                END AS esElectiva
            FROM ACADEMICO.PENSUMMATERIA PEMA
            INNER JOIN ACADEMICO.PENSUM PENS
                ON PENS.PENS_ID = PEMA.PENS_ID
            INNER JOIN ACADEMICO.MATERIA MATE
                ON MATE.MATE_CODIGOMATERIA = PEMA.MATE_CODIGOMATERIA
            WHERE PEMA.PENS_ID = :idPensum
                AND NOT EXISTS (
                    SELECT 1
                    FROM RVDIES.MATERIAPARTICULAR MAPA
                    WHERE MAPA.PROG_ID = PENS.PROG_ID
                        AND TO_CHAR(MAPA.MATE_CODIGOMATERIA) = PEMA.MATE_CODIGOMATERIA
                )
            ORDER BY PEMA.PEMA_PERIODO, MATE.MATE_NOMBRE
            """, nativeQuery = true)
    List<MateriaPensumProjection> findByIdPensum(@Param("idPensum") Long idPensum);
}
