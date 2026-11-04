package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.mapper.PersonaAutorizaConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaRelacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.PeriodoUniversidadService;
import co.edu.unipamplona.ciadti.rvd.model.service.TipoVinculacionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/preload-call")
public class PreloadCallController {

    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;
    private final TipoVinculacionService tipoVinculacionService;
    private final PeriodoUniversidadService periodoUniversidadService;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final PersonaAutorizaConvocatoriaMapper personaAutorizaConvocatoriaMapper;

    @Operation(summary = "Obtiene la lista de convocatorias")
    @GetMapping("/list")
    public ResponseEntity<List<ConvocatoriaDTO>> callList(@RequestParam(required = false) Long idPeriodoUniversidad) {
        List<ConvocatoriaDTO> callList = convocatoriaPrecargaService.findCallListWithDates(idPeriodoUniversidad);
        return new ResponseEntity<>(callList, HttpStatus.OK);
    }

    @Operation(summary = "Busca personas generales por documento o nombre")
    @GetMapping("/search-general-person")
    public ResponseEntity<List<PersonaAutorizaConvocatoriaDTO>> searchGeneralPerson(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String documento) {
        List<PersonaAutorizaConvocatoriaDTO> personas = personaGeneralRepository.searchGeneralPerson(nombre, documento).stream()
                .map(personaAutorizaConvocatoriaMapper::toDto)
                .toList();
        return ResponseEntity.ok(personas);
    }

    @Operation(summary = "Obtiene la lista de periodos de universidad")
    @GetMapping("/list-university-period")
    public ResponseEntity<List<PeriodoUniversidadDTO>> listUniversityPeriod() {
        List<PeriodoUniversidadDTO> periods = periodoUniversidadService.findUniversityPeriodList();
        return new ResponseEntity<>(periods, HttpStatus.OK);
    }

    @Operation(summary = "Obtiene la lista de tipos de vinculación activos")
    @GetMapping("/list-tipo-vinculacion")
    public ResponseEntity<List<TipoVinculacionDTO>> listTipoVinculacion() {
        List<TipoVinculacionDTO> vinculaciones = tipoVinculacionService.listActive();
        return new ResponseEntity<>(vinculaciones, HttpStatus.OK);
    }

    @Operation(summary = "Obtiene la lista de clasificaciones docentes")
    @GetMapping("/list-clasificacion-docente")
    public ResponseEntity<List<co.edu.unipamplona.ciadti.rvd.model.dto.ClasificacionDocenteDTO>> listClasificacionDocente() {
        return ResponseEntity.ok(tipoVinculacionService.listClasificacionesDocente());
    }

    @Operation(summary = "Obtiene la lista de dedicaciones docentes")
    @GetMapping("/list-dedicacion-docente")
    public ResponseEntity<List<co.edu.unipamplona.ciadti.rvd.model.dto.DedicacionDocenteDTO>> listDedicacionDocente() {
        return ResponseEntity.ok(tipoVinculacionService.listDedicacionesDocente());
    }

    @Operation(summary = "Obtiene los tipos de horas permitidos (Semanal, Semestral)")
    @GetMapping("/list-tipo-horas")
    public ResponseEntity<List<co.edu.unipamplona.ciadti.rvd.model.dto.TipoHorasDTO>> listTipoHoras() {
        return ResponseEntity.ok(tipoVinculacionService.listTiposHoras());
    }

    @Operation(summary = "Crea un nuevo tipo de vinculación en el catálogo")
    @PostMapping("/create-tipo-vinculacion")
    public ResponseEntity<TipoVinculacionDTO> createTipoVinculacion(@RequestBody TipoVinculacionDTO dto) {
        TipoVinculacionDTO created = tipoVinculacionService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Guarda una nueva convocatoria con sus tipos de vinculación")
    @PostMapping("/save")
    public ResponseEntity<Void> saveCall(@RequestBody ConvocatoriaFormularioDTO dto) {
        convocatoriaPrecargaService.save(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtiene el detalle de una convocatoria")
    @GetMapping("/detail/{id}")
    public ResponseEntity<ConvocatoriaFormularioDTO> detailCall(@PathVariable Long id) {
        ConvocatoriaFormularioDTO detail = convocatoriaPrecargaService.findCallDetail(id);
        return ResponseEntity.ok(detail);
    }

    @Operation(summary = "Actualiza una convocatoria y sus tipos de vinculación")
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateCall(@PathVariable Long id, @RequestBody ConvocatoriaFormularioDTO dto) {
        convocatoriaPrecargaService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Elimina una convocatoria")
    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCall(@PathVariable Long id, @RequestBody(required = false) ConvocatoriaFormularioDTO dto) {
        convocatoriaPrecargaService.delete(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Elimina convocatorias masivamente")
    @PostMapping("/delete-bulk")
    public ResponseEntity<Void> bulkDeleteCall(@RequestBody List<ConvocatoriaFormularioDTO> listaConvocatorias) {
        convocatoriaPrecargaService.bulkDelete(listaConvocatorias);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtiene la lista de convocatorias de un primer periodo por año")
    @GetMapping("/list-by-first-period-by-year")
    public ResponseEntity<List<ConvocatoriaDTO>> listPreloadCallByFirstPeriodByYear(@RequestParam Long year) {
        List<ConvocatoriaDTO> callList = convocatoriaPrecargaService.findCallListByFirstPeriodByYear(year);
        return new ResponseEntity<>(callList, HttpStatus.OK);
    }

    @Operation(summary = "Relaciona una convocatoria con otra convocatoria padre")
    @PutMapping("/update-relation/{idConvocatoria}")
    public ResponseEntity<Void> updateRelation(@PathVariable Long idConvocatoria, @RequestBody ConvocatoriaRelacionDTO dto) {
        convocatoriaPrecargaService.updateRelation(idConvocatoria, dto.idRelacion());
        return ResponseEntity.ok().build();
    }
}
