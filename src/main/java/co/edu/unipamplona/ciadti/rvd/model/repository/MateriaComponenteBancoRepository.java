/**
 * Aplicación: rvd
 * Archivo: MateriaComponenteBancoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Listado de componentes por banco de materias.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.MateriaComponenteBancoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.MateriaComponenteBancoEntityId;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaComponenteBancoProjection;

public interface MateriaComponenteBancoRepository
        extends JpaRepository<MateriaComponenteBancoEntity, MateriaComponenteBancoEntityId> {

    @Query(value = """
            SELECT
                MATE.MATE_CODIGOMATERIA AS codigoMateria,
                MATE.MATE_NOMBRE AS nombre,
                MATE.MATE_HORASPRACTICAS AS horasPracticas,
                MATE.MATE_HORASTEORICAS AS horasTeoricas,
                MATE.MATE_HORASTEORICOPRACTICAS AS horasTeoricoPracticas,
                MATE.MATE_PONDERACIONACADEMICA AS ponderacionAcademica,
                MATE.MATE_CAPACIDAD AS capacidad,
                MACB.MACB_PESO AS peso,
                MACB.MACB_ACTIVA AS estado
            FROM ACADEMICO.MATERIACOMPONENTEBANCO MACB
            INNER JOIN ACADEMICO.MATERIA MATE
                ON MATE.MATE_CODIGOMATERIA = MACB.MACB_SUBMATERIABANCO
            WHERE MACB.MACB_BANCOMATERIA = :codigoMateria
            ORDER BY MATE.MATE_NOMBRE
            """, nativeQuery = true)
    List<MateriaComponenteBancoProjection> findByCodigoBanco(@Param("codigoMateria") String codigoMateria);
}
