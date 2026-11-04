package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.TipoVinculacionConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoVinculacionListaProjection;

public interface TipoVinculacionConvocatoriaRepository extends JpaRepository<TipoVinculacionConvocatoriaEntity, Long> {

    @Query(value = """
            SELECT
                TIVI.TIVI_ID AS id,
                CLDO.CLDO_ID AS idClasificacionDocente,
                CLDO.CLDO_DESCRIPCION AS descripcionClasificacionDocente,
                DEDO.DEDO_ID AS idDedicacionDocente,
                DEDO.DEDO_DESCRIPCION AS descripcionDedicacionDocente,
                TIVI.TIVI_MINIMO AS minimo,
                TIVI.TIVI_MAXIMO AS maximo,
                TIVI.TIVI_FORMAPAGO AS formaPago,
                TIVI.TIVI_TIPOCONTRATO AS tipoContrato,
                TIVI.TIVI_ESPLANTA AS esPlanta,
                TIVI.TIVI_TIPOHORAS AS tipoHoras
            FROM RVDIES.TIPOVINCULACIONCONVOCATORIA TIVC
            INNER JOIN RVDIES.TIPOVINCULACION TIVI
                ON TIVI.TIVI_ID = TIVC.TIVI_ID
            LEFT JOIN ACADEMICO.CLASIFICACIONDOCENTE CLDO
                ON CLDO.CLDO_ID = TIVI.CLDO_ID
            LEFT JOIN ACADEMICO.DEDICACIONDOCENTE DEDO
                ON DEDO.DEDO_ID = TIVI.DEDO_ID
            WHERE TIVC.CONV_ID = :idConvocatoria
            ORDER BY TIVI.TIVI_ID
            """, nativeQuery = true)
    List<TipoVinculacionListaProjection> findByIdConvocatoria(
            @Param("idConvocatoria") Long idConvocatoria);

    @Query("SELECT t FROM TipoVinculacionConvocatoriaEntity t LEFT JOIN FETCH t.tipoVinculacion tv LEFT JOIN FETCH tv.clasificacionDocente LEFT JOIN FETCH tv.dedicacionDocente WHERE t.idConvocatoria = :idConvocatoria")
    List<TipoVinculacionConvocatoriaEntity> findByIdConvocatoriaWithVinculacion(@Param("idConvocatoria") Long idConvocatoria);

    @Modifying
    @Query("DELETE FROM TipoVinculacionConvocatoriaEntity t WHERE t.idConvocatoria = :idConvocatoria")
    void deleteByIdConvocatoria(@Param("idConvocatoria") Long idConvocatoria);
}
