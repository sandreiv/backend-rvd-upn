/**
 * Aplicación: rvd
 * Archivo: BasicTablesController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 24/11/2026
 * Modificaciones:
 * 24/11/2026 - Creación inicial para administración de tablas básicas (NOVEDADES)
 * 26/11/2026 - Se agrega VINCULACIONACTIVIDADES al mismo controlador: el mapping de
 *              clase baja a /basic-tables y cada tabla queda como subruta
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
import co.edu.unipamplona.ciadti.rvd.model.dto.OpcionAgrupadaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.VinculacionActividadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.VinculacionActividadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.NovedadAdministracionService;
import co.edu.unipamplona.ciadti.rvd.model.service.VinculacionActividadAdministracionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * Administración de tablas básicas.
 *
 * Todas las tablas cuelgan de /configuration/administration/basic-tables/<tabla>
 * para que el padre 03 (/configuration/administration/**) siga cubriendo la
 * autorización por METHOD:URL. Cada tabla expone el mismo contrato:
 * /list, /save, /update/{id}, /delete/{id} y /delete-bulk.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/administration/basic-tables")
public class BasicTablesController {

    private final NovedadAdministracionService novedadAdministracionService;

    private final VinculacionActividadAdministracionService
            vinculacionActividadAdministracionService;

    // =====================================================================
    // NOVEDADES (RVDIES.NOVEDADES)
    // =====================================================================

    @Operation(
            summary = "Lista las novedades",
            description = "Lista las novedades registradas en RVDIES.NOVEDADES"
    )
    @GetMapping("/novedades/list")
    public ResponseEntity<List<NovedadListadoDTO>> listNovedades() {
        return ResponseEntity.ok(
                novedadAdministracionService.listNovedades()
        );
    }

    @Operation(
            summary = "Registra una novedad",
            description = "Crea una novedad en el catálogo de tablas básicas"
    )
    @PostMapping("/novedades/save")
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
    @PutMapping("/novedades/update/{id}")
    public ResponseEntity<Void> updateNovedad(
            @PathVariable Long id,
            @RequestBody NovedadFormularioDTO dto
    ) {
        novedadAdministracionService.updateNovedad(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Elimina una novedad",
            description = "Elimina una novedad del catálogo tras validar que no esté en uso"
    )
    @DeleteMapping("/novedades/delete/{id}")
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
    @PostMapping("/novedades/delete-bulk")
    public ResponseEntity<Void> deleteBulkNovedades(
            @RequestBody EliminacionMasivaDTO dto
    ) {
        novedadAdministracionService.deleteBulkNovedades(dto.ids());
        return ResponseEntity.ok().build();
    }

    // =====================================================================
    // VINCULACIÓN DE ACTIVIDADES (RVDIES.VINCULACIONACTIVIDADES)
    // =====================================================================

    @Operation(
            summary = "Lista las vinculaciones de actividades",
            description = "Lista RVDIES.VINCULACIONACTIVIDADES con el tipo de vinculación y la actividad"
    )
    @GetMapping("/vinculacion-actividades/list")
    public ResponseEntity<List<VinculacionActividadListadoDTO>> listVinculacionActividades() {
        return ResponseEntity.ok(
                vinculacionActividadAdministracionService
                        .listVinculacionActividades()
        );
    }

    @Operation(
            summary = "Catálogo de tipos de vinculación",
            description = "Tipos de vinculación disponibles para el formulario"
    )
    @GetMapping("/vinculacion-actividades/catalogs/tipos-vinculacion")
    public ResponseEntity<List<OpcionAgrupadaDTO>> listTiposVinculacion() {
        return ResponseEntity.ok(
                vinculacionActividadAdministracionService
                        .listTiposVinculacion()
        );
    }

    @Operation(
            summary = "Catálogo de actividades",
            description = "Actividades activas seleccionables, agrupadas por actividad padre"
    )
    @GetMapping("/vinculacion-actividades/catalogs/tipos-actividad")
    public ResponseEntity<List<OpcionAgrupadaDTO>> listTiposActividad() {
        return ResponseEntity.ok(
                vinculacionActividadAdministracionService
                        .listTiposActividad()
        );
    }

    @Operation(
            summary = "Registra una vinculación de actividad",
            description = "Crea la relación entre un tipo de vinculación y una actividad"
    )
    @PostMapping("/vinculacion-actividades/save")
    public ResponseEntity<Void> saveVinculacionActividad(
            @RequestBody VinculacionActividadFormularioDTO dto
    ) {
        vinculacionActividadAdministracionService
                .saveVinculacionActividad(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Actualiza una vinculación de actividad",
            description = "Actualiza la relación entre un tipo de vinculación y una actividad"
    )
    @PutMapping("/vinculacion-actividades/update/{id}")
    public ResponseEntity<Void> updateVinculacionActividad(
            @PathVariable Long id,
            @RequestBody VinculacionActividadFormularioDTO dto
    ) {
        vinculacionActividadAdministracionService
                .updateVinculacionActividad(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Elimina una vinculación de actividad",
            description = "Elimina el registro por su identificador"
    )
    @DeleteMapping("/vinculacion-actividades/delete/{id}")
    public ResponseEntity<Void> deleteVinculacionActividad(
            @PathVariable Long id
    ) {
        vinculacionActividadAdministracionService
                .deleteVinculacionActividad(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Elimina masivamente vinculaciones de actividad",
            description = "Elimina varios registros según los identificadores indicados"
    )
    @PostMapping("/vinculacion-actividades/delete-bulk")
    public ResponseEntity<Void> deleteBulkVinculacionActividades(
            @RequestBody EliminacionMasivaDTO dto
    ) {
        vinculacionActividadAdministracionService
                .deleteBulkVinculacionActividades(dto.ids());
        return ResponseEntity.ok().build();
    }
}
