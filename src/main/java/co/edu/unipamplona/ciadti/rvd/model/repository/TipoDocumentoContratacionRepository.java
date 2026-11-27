/**
 * Aplicación: rvd
 * Archivo: TipoDocumentoContratacionRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/11/2026
 * Modificaciones:
 * 27/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.TipoDocumentoContratacionEntity;

public interface TipoDocumentoContratacionRepository extends JpaRepository<TipoDocumentoContratacionEntity, Long> {
    
    boolean existsByNombre(String nombre);
    
    @Query(value = """
            SELECT TIDC.*
            FROM RVDIES.TIPODOCUMENTOCONTRATACION TIDC
            ORDER BY TIDC.TIDC_NOMBRE ASC
        """, nativeQuery = true)
    List<TipoDocumentoContratacionEntity> findAllDocumentTypes();
}
