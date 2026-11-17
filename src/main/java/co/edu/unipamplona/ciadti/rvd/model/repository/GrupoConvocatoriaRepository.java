/**
 * Aplicación: rvd
 * Archivo: GrupoConvocatoriaRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.GrupoConvocatoriaEntity;

public interface GrupoConvocatoriaRepository extends JpaRepository<GrupoConvocatoriaEntity, Long> {

    @Query("SELECT gc FROM GrupoConvocatoriaEntity gc LEFT JOIN FETCH gc.convocatoria c LEFT JOIN FETCH c.periodoUniversidad WHERE gc.idProyectoGrupo = :idProyectoGrupo ORDER BY gc.id ASC")
    List<GrupoConvocatoriaEntity> findByIdProyectoGrupoWithConvocatoria(@Param("idProyectoGrupo") Long idProyectoGrupo);

    List<GrupoConvocatoriaEntity> findByIdProyectoGrupo(Long idProyectoGrupo);

    @Modifying
    @Query("DELETE FROM GrupoConvocatoriaEntity gc WHERE gc.idProyectoGrupo = :idProyectoGrupo")
    void deleteByIdProyectoGrupo(@Param("idProyectoGrupo") Long idProyectoGrupo);

    @Query("SELECT gc.idConvocatoria FROM GrupoConvocatoriaEntity gc WHERE gc.idProyectoGrupo = :idProyectoGrupo AND gc.estado = '1'")
    List<Long> findActiveConvocatoriaIdsByProyectoGrupo(@Param("idProyectoGrupo") Long idProyectoGrupo);
}
