/**
 * Aplicación: rvd
 * Archivo: ProjectsController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 09/11/2026 - Leonel Antonio Perez Rios - Creación inicial
 * 10/11/2026 - Leonel Antonio Perez Rios - Endpoint de búsqueda de personas para proyectos
 * 10/11/2026 - Leonel Antonio Perez Rios - Endpoint para listar todos los proyectos jerárquicamente
 * 10/11/2026 - Leonel Antonio Perez Rios - Refactorización de código limpio, remoción de imports comodín y estandarización a subproyectos
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DiaSemanaOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminacionMasivaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FuncionProyectoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NivelProyectoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoGrupoOpcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ProyectosService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/administration/projects")
public class ProjectsController {

    private final ProyectosService proyectosService;

    @Operation(summary = "Lista todos los proyectos y subproyectos", description = "Lista todos los proyectos jerárquicamente para visualización en árbol")
    @GetMapping("/all")
    public ResponseEntity<List<ProyectosListaDTO>> listAllProjects() {
        return ResponseEntity.ok(proyectosService.listAllProjects());
    }

    @Operation(summary = "Lista proyectos padre", description = "Lista únicamente los proyectos principales con PROY_IDPROYECTO nulo")
    @GetMapping("/list")
    public ResponseEntity<List<ProyectosListaDTO>> listProjects() {
        return ResponseEntity.ok(proyectosService.listProjects());
    }

    @Operation(summary = "Lista subproyectos", description = "Lista subproyectos o productos asociados al proyecto padre indicado")
    @GetMapping({"/list-products", "/list-subprojects"})
    public ResponseEntity<List<ProyectosListaDTO>> listProducts(@RequestParam Long idProyecto) {
        return ResponseEntity.ok(proyectosService.listProducts(idProyecto));
    }

    @Operation(summary = "Lista tipos de actividad para proyectos", description = "Lista las actividades organizadas jerárquicamente con indicación de seleccionabilidad")
    @GetMapping("/activity-types")
    public ResponseEntity<List<TipoActividadProyectoDTO>> listProjectActivityTypes() {
        return ResponseEntity.ok(proyectosService.listProjectActivityTypes());
    }

    @Operation(summary = "Busca coordinaciones para proyectos", description = "Busca coordinaciones activas por nombre de unidad de área o identificador")
    @GetMapping("/coordinations")
    public ResponseEntity<List<CoordinacionOpcionDTO>> searchCoordinations(@RequestParam(required = false) String term) {
        return ResponseEntity.ok(proyectosService.searchCoordinations(term));
    }

    @Operation(summary = "Lista grupos de proyectos", description = "Lista los grupos de proyectos activos para el selector de proyectos")
    @GetMapping("/groups")
    public ResponseEntity<List<ProyectoGrupoOpcionDTO>> listProjectGroups() {
        return ResponseEntity.ok(proyectosService.listProjectGroupOptions());
    }

    @Operation(summary = "Lista centros de costo", description = "Lista los centros de costo disponibles para participantes de proyectos según coordinación")
    @GetMapping("/cost-centers")
    public ResponseEntity<List<CentroCostoOpcionDTO>> listCostCenters(
            @RequestParam(required = false) Long idProyecto,
            @RequestParam(required = false) Long idCoordinacion) {
        return ResponseEntity.ok(proyectosService.listCostCenters(idProyecto, idCoordinacion));
    }

    @Operation(
            summary = "Lista funciones de participantes",
            description = "Lista las funciones disponibles para participantes de proyectos desde enum de backend"
    )
    @GetMapping("/participant-functions")
    public ResponseEntity<List<FuncionProyectoOpcionDTO>> listParticipantFunctions() {
        return ResponseEntity.ok(proyectosService.listParticipantFunctions());
    }

    @Operation(
            summary = "Lista niveles de proyectos",
            description = "Lista los niveles disponibles para proyectos y participantes desde enum de backend"
    )
    @GetMapping("/participant-levels")
    public ResponseEntity<List<NivelProyectoOpcionDTO>> listParticipantLevels() {
        return ResponseEntity.ok(proyectosService.listParticipantLevels());
    }

    @Operation(
            summary = "Lista días de la semana",
            description = "Lista los días estándar de la semana para asignación de horarios desde enum de backend"
    )
    @GetMapping("/days-of-week")
    public ResponseEntity<List<DiaSemanaOpcionDTO>> listDaysOfWeek() {
        return ResponseEntity.ok(proyectosService.listDaysOfWeek());
    }

    @Operation(summary = "Guarda proyecto o subproyecto", description = "Crea un proyecto principal (idProyectoPadre nulo) o subproyecto (con padre)")
    @PostMapping("/save")
    public ResponseEntity<Void> saveProject(@RequestBody ProyectosFormularioDTO dto) {
        proyectosService.saveProject(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualiza proyecto o subproyecto", description = "Actualiza un proyecto o subproyecto existente")
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateProject(@PathVariable Long id, @RequestBody ProyectosFormularioDTO dto) {
        proyectosService.updateProject(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Elimina proyecto o subproyecto", description = "Elimina el proyecto si no tiene subproyectos ni participantes asociados")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        proyectosService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Elimina proyectos masivamente", description = "Eliminación masiva de proyectos o subproyectos")
    @PostMapping("/delete-bulk")
    public ResponseEntity<Void> deleteBulkProjects(@RequestBody EliminacionMasivaDTO dto) {
        proyectosService.deleteBulkProjects(dto.ids());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Lista participantes de un proyecto", description = "Lista docentes asignados a un proyecto")
    @GetMapping({"/participants/list", "/list-persons"})
    public ResponseEntity<List<PersonaProyectoListaDTO>> listProjectParticipants(@RequestParam Long idProyecto) {
        return ResponseEntity.ok(proyectosService.listProjectParticipants(idProyecto));
    }

    @Operation(summary = "Busca docentes para proyectos", description = "Busca personas docentes por nombre o documento para asignar al proyecto")
    @GetMapping({"/persons/search", "/search-persons", "/participants/search"})
    public ResponseEntity<List<PersonaAutorizaConvocatoriaDTO>> searchPersons(@RequestParam(required = false) String term, @RequestParam(required = false) String nombre, @RequestParam(required = false) String documento) {
        String query = term != null ? term : (nombre != null ? nombre : documento);
        return ResponseEntity.ok(proyectosService.searchPersons(query));
    }

    @Operation(summary = "Asigna docente a proyecto", description = "Registra un docente participante en el proyecto")
    @PostMapping({"/participants/save", "/save-person"})
    public ResponseEntity<Void> saveProjectParticipant(@RequestBody PersonaProyectoFormularioDTO dto) {
        proyectosService.saveProjectParticipant(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualiza participante de proyecto", description = "Actualiza los datos del docente participante en el proyecto")
    @PutMapping({"/participants/update/{id}", "/update-person/{id}"})
    public ResponseEntity<Void> updateProjectParticipant(@PathVariable Long id, @RequestBody PersonaProyectoFormularioDTO dto) {
        proyectosService.updateProjectParticipant(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Elimina participante de proyecto", description = "Elimina la asignación de un docente a un proyecto")
    @DeleteMapping({"/participants/delete/{id}", "/delete-person/{id}"})
    public ResponseEntity<Void> deleteProjectParticipant(@PathVariable Long id) {
        proyectosService.deleteProjectParticipant(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Elimina participantes masivamente", description = "Eliminación masiva de participantes de proyecto")
    @PostMapping({"/participants/delete-bulk", "/delete-persons-bulk"})
    public ResponseEntity<Void> deleteBulkProjectParticipants(@RequestBody EliminacionMasivaDTO dto) {
        proyectosService.deleteBulkProjectParticipants(dto.ids());
        return ResponseEntity.ok().build();
    }
}
