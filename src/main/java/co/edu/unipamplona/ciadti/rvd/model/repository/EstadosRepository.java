/**
 * Aplicación: rvd
 * Archivo: EstadosRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/11/2026
 * Modificaciones:
 * 06/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.EstadosEntity;

public interface EstadosRepository
        extends JpaRepository<EstadosEntity, Long> {

    Optional<EstadosEntity> findFirstByOrden(String orden);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT e
            FROM EstadosEntity e
            WHERE e.id = :id
            """)
    Optional<EstadosEntity> findByIdForUpdate(
            @Param("id") Long id
    );

}
