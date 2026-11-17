/**
 * Aplicación: rvd
 * Archivo: ClaseSemanalRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/11/2026
 * Modificaciones:
 * 13/11/2026 - Horario de clase por grupo.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ClaseSemanalEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorarioClaseProjection;

public interface ClaseSemanalRepository extends JpaRepository<ClaseSemanalEntity, Long> {

    @Query(value = """
            SELECT
                clse.CLSE_ID AS id,
                clse.CLSE_DIA AS dia,
                clse.CLSE_FECHAINICIO AS fechaInicio,
                clse.CLSE_FECHAFINAL AS fechaFinal,
                inicial.BLHO_ID AS idBloqueInicial,
                inicial.BLHO_NOMBRE AS nombreBloqueInicial,
                inicial.BLHO_HORAINICIO AS horaInicioBloqueInicial,
                inicial.BLHO_HORAFINAL AS horaFinalBloqueInicial,
                finalb.BLHO_ID AS idBloqueFinal,
                finalb.BLHO_NOMBRE AS nombreBloqueFinal,
                finalb.BLHO_HORAINICIO AS horaInicioBloqueFinal,
                finalb.BLHO_HORAFINAL AS horaFinalBloqueFinal
            FROM ACADEMICO.CLASESEMANAL clse
            LEFT JOIN ACADEMICO.BLOQUEHORAS inicial
                ON inicial.BLHO_ID = clse.BLHO_IDINICIAL
            LEFT JOIN ACADEMICO.BLOQUEHORAS finalb
                ON finalb.BLHO_ID = clse.BLHO_IDFINAL
            WHERE clse.GRUP_ID = :idGrupo
            ORDER BY clse.CLSE_DIA, inicial.BLHO_HORAINICIO, clse.CLSE_ID
            """, nativeQuery = true)
    List<HorarioClaseProjection> findHorarioByIdGrupo(@Param("idGrupo") Long idGrupo);
}
