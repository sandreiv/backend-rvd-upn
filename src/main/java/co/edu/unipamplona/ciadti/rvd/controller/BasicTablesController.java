/**
 * Aplicación: rvd
 * Archivo: BasicTablesController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 24/11/2026
 * Modificaciones:
 * 24/11/2026 - Creación inicial para administración de tablas básicas (NOVEDADES)
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.EliminacionMasivaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.NovedadAdministracionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * Administración de tablas básicas.
 *
 * Cada tabla básica cuelga de /configuration/administration/basic-tables/<tabla>
 * para que el padre 03 (/configuration/administration/**) siga cubriendo la
 * autorización por METHOD:URL.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/administration/basic-tables/novedades")
public class BasicTablesController {

    private final NovedadAdministracionService novedadAdministracionService;

    @Operation(
            summary = "Lista las novedades",
            description = "Lista las novedades registradas en RVD.NOVEDADES"
    )
    @GetMapping("/list")
    public ResponseEntity<List<NovedadListadoDTO>> listNovedades() {
        return ResponseEntity.ok(
                novedadAdministracionService.listNovedades()
        );
    }

    @Operation(
            summary = "Registra una novedad",
            description = "Crea una novedad en el catálogo de tablas básicas"
    )
    @PostMapping("/save")
    public ResponseEntity<Void> saveNovedad(
            @RequestBody NovedadFormularioDTO dto
    ) {
        novedadAdministracionService.saveNovedad(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Actualiza una novedad",
            description = "Actualiza una novedad existente del catálogo"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateNovedad(
            @PathVariable Long id,
            @RequestBody NovedadFormularioDTO dto
    ) {
        novedadAdministracionService.updateNovedad(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Elimina una novedad",
            description = "Elimina una novedad a través del procedimiento RVD.PR_RVD_D_NOVEDADES"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNovedad(
            @PathVariable Long id
    ) {
        novedadAdministracionService.deleteNovedad(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Elimina masivamente novedades",
            description = "Elimina varias novedades según los identificadores indicados"
    )
    @PostMapping("/delete-bulk")
    public ResponseEntity<Void> deleteBulkNovedades(
            @RequestBody EliminacionMasivaDTO dto
    ) {
        novedadAdministracionService.deleteBulkNovedades(dto.ids());
        return ResponseEntity.ok().build();
    }
}
