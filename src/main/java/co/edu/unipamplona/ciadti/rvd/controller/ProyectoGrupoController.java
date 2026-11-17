/**
 * Aplicación: rvd
 * Archivo: ProyectoGrupoController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/11/2026
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ProyectoGrupoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuration/administration/project-groups")
public class ProyectoGrupoController {

    private final ProyectoGrupoService proyectoGrupoService;

    @Operation(summary = "Lista todos los grupos de proyectos", description = "Lista los grupos de proyectos con sus convocatorias asociadas")
    @GetMapping({"", "/list"})
    public ResponseEntity<List<ProyectoGrupoListaDTO>> listProjectGroups() {
        return ResponseEntity.ok(proyectoGrupoService.listProjectGroups());
    }

    @Operation(summary = "Obtiene un grupo de proyectos por ID", description = "Obtiene los detalles del grupo de proyectos y sus convocatorias asignadas")
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoGrupoDTO> getProjectGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoGrupoService.getProjectGroupById(id));
    }

    @Operation(summary = "Crea un nuevo grupo de proyectos", description = "Registra un grupo de proyectos y asocia sus convocatorias")
    @PostMapping
    public ResponseEntity<Void> saveProjectGroup(@RequestBody ProyectoGrupoFormularioDTO dto) {
        proyectoGrupoService.saveProjectGroup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Actualiza un grupo de proyectos", description = "Actualiza la información del grupo y sincroniza las convocatorias asociadas")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProjectGroup(@PathVariable Long id, @RequestBody ProyectoGrupoFormularioDTO dto) {
        proyectoGrupoService.updateProjectGroup(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Elimina un grupo de proyectos", description = "Elimina el grupo de proyectos si no tiene proyectos vinculados")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectGroup(@PathVariable Long id) {
        proyectoGrupoService.deleteProjectGroup(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista opciones de grupos de proyectos", description = "Lista grupos activos para selectores en formularios de proyectos")
    @GetMapping("/options")
    public ResponseEntity<List<ProyectoGrupoOpcionDTO>> listProjectGroupOptions() {
        return ResponseEntity.ok(proyectoGrupoService.listProjectGroupOptions());
    }

    @Operation(summary = "Lista convocatorias disponibles", description = "Lista todas las convocatorias para asignación a grupos")
    @GetMapping("/available-convocatorias")
    public ResponseEntity<List<ConvocatoriaOpcionDTO>> listAvailableConvocatorias() {
        return ResponseEntity.ok(proyectoGrupoService.listAvailableConvocatorias());
    }
}
