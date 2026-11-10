/**
 * Aplicación: rvd
 * Archivo: ActivityTypesController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/11/2026
 * Modificaciones:
 * 10/11/2026 - Leonel Antonio Perez Rios - Creación inicial de endpoints para administración de tipos de actividad
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
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadAdministracionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadAdministracionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.TipoActividadesAdministracionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/administration/activity-types")
public class ActivityTypesController {

    private final TipoActividadesAdministracionService tipoActividadesAdministracionService;

    @Operation(
            summary = "Lista tipos de actividad padre",
            description = "Lista los tipos de actividad principales de administración"
    )
    @GetMapping("/list")
    public ResponseEntity<List<TipoActividadAdministracionListadoDTO>> listActivityTypes() {
        return ResponseEntity.ok(tipoActividadesAdministracionService.listParentActivityTypes());
    }

    @Operation(
            summary = "Lista tipos de actividad hijas",
            description = "Lista los tipos de actividad hijas correspondientes al tipo padre indicado"
    )
    @GetMapping("/{idPadre}/children/list")
    public ResponseEntity<List<TipoActividadAdministracionListadoDTO>> listChildActivityTypes(@PathVariable Long idPadre) {
        return ResponseEntity.ok(tipoActividadesAdministracionService.listChildActivityTypes(idPadre));
    }

    @Operation(
            summary = "Registra un tipo de actividad padre",
            description = "Crea un nuevo tipo de actividad principal"
    )
    @PostMapping("/save")
    public ResponseEntity<Void> saveActivityType(@RequestBody TipoActividadAdministracionFormularioDTO dto) {
        tipoActividadesAdministracionService.saveActivityType(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Registra un tipo de actividad hija",
            description = "Crea un nuevo tipo de actividad asociado a un tipo de actividad padre"
    )
    @PostMapping("/{idPadre}/children/save")
    public ResponseEntity<Void> saveChildActivityType(
            @PathVariable Long idPadre,
            @RequestBody TipoActividadAdministracionFormularioDTO dto
    ) {
        tipoActividadesAdministracionService.saveChildActivityType(idPadre, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Actualiza un tipo de actividad",
            description = "Actualiza la información de un tipo de actividad existente"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateActivityType(
            @PathVariable Long id,
            @RequestBody TipoActividadAdministracionFormularioDTO dto
    ) {
        tipoActividadesAdministracionService.updateActivityType(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Elimina un tipo de actividad",
            description = "Elimina un tipo de actividad si no tiene dependencias activas"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteActivityType(@PathVariable Long id) {
        tipoActividadesAdministracionService.deleteActivityType(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Eliminación masiva de tipos de actividad",
            description = "Elimina varios tipos de actividad por lista de identificadores"
    )
    @PostMapping("/delete-bulk")
    public ResponseEntity<Void> deleteBulkActivityTypes(@RequestBody EliminacionMasivaDTO dto) {
        tipoActividadesAdministracionService.deleteBulkActivityTypes(dto.ids());
        return ResponseEntity.ok().build();
    }
}
