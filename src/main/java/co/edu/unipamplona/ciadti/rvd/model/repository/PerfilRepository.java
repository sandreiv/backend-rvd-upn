package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PerfilEntity;

public interface PerfilRepository
        extends JpaRepository<PerfilEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM PerfilEntity p
            WHERE p.id = :id
            """)
    Optional<PerfilEntity> findByIdForUpdate(
            @Param("id") Long id
    );

}