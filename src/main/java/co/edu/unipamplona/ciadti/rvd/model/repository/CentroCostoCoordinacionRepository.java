/**
 * Aplicación: rvd
 * Archivo: CentroCostoCoordinacionRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/11/2026
 * Modificaciones:
 * 25/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.CentroCostoCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CentroCostoAsignadoListadoProjection;

public interface CentroCostoCoordinacionRepository extends JpaRepository<CentroCostoCoordinacionEntity, Long> {

    Optional<CentroCostoCoordinacionEntity> findFirstByIdCoordinacion(Long idCoordinacion);

    void deleteByIdCoordinacion(Long idCoordinacion);

    @Query(value = """
            SELECT
                CCCO.CCCO_ID AS id,
                COOR.COOR_ID AS idCoordinacion,
                UNID_AREA.UNID_NOMBRE AS coordinacion,
                CECO.CECO_ID AS idCentroCosto,
                CECO.CECO_DESCRIPCION AS centroCosto,
                TO_CHAR(CCCO.CCCO_ESTADO) AS estado
            FROM RVDIES.CENTROCOSTOCOORDINACION CCCO
            LEFT JOIN RVDIES.COORDINACIONES COOR
                ON COOR.COOR_ID = CCCO.COOR_ID
            LEFT JOIN ACADEMICO.UNIDAD UNID_AREA
                ON UNID_AREA.UNID_ID = COOR.UNID_IDAREA
            LEFT JOIN CONTABLEV3.CENTROCOSTO CECO
                ON CECO.CECO_ID = CCCO.CECO_ID
            ORDER BY UNID_AREA.UNID_NOMBRE, CECO.CECO_DESCRIPCION
            """, nativeQuery = true)
    List<CentroCostoAsignadoListadoProjection> findAdministrationList();

    @Query(value = """
            SELECT CCCO.CCCO_ID
            FROM RVDIES.CENTROCOSTOCOORDINACION CCCO
            WHERE CCCO.COOR_ID = :idCoordinacion
            """, nativeQuery = true)
    List<Long> findIdsByIdCoordinacion(@Param("idCoordinacion") Long idCoordinacion);

    @Query(value = """
            SELECT ascc.CECO_ID
            FROM RVD.ASIGNARCENTROCOSTO ascc
            INNER JOIN RVD.COORDINACIONES coor
                ON coor.COOR_ID = ascc.COOR_ID
            WHERE UPPER(TRIM(coor.COOR_CODIGO)) = UPPER(TRIM(:codigoCoordinacion))
                AND ascc.CECO_ID IS NOT NULL
                AND ROWNUM = 1
            """, nativeQuery = true)
    Optional<Long> findIdCentroCostoByCodigoCoordinacion(
            @Param("codigoCoordinacion") String codigoCoordinacion);

}
