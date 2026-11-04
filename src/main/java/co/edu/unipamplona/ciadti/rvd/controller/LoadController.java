/**
 * Aplicación: rvd
 * Archivo: ProfessorPreloadController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial

 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.CargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.PeriodoUniversidadService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/load")
public class LoadController {

    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;
    private final PeriodoUniversidadService periodoUniversidadService;
    private final CargaService cargaService;
    
    @Operation(
        summary = "Obtiene las convocatorias activas",
        description = "Obtiene las convocatorias."
    )
    @GetMapping("/list-active-calls")
    public ResponseEntity<List<ConvocatoriaDTO>> listActivePreloadCalls(@RequestParam Long idPeriodoUniversidad) {
        return ResponseEntity.ok(convocatoriaPrecargaService.findActivePreloadCalls(idPeriodoUniversidad));
    }

    @Operation(
        summary = "Obtiene la lista de periodos de universidad",
        description = "Obtiene la lista de periodos de universidad"
    )
    @GetMapping("/list-university-period")
    public ResponseEntity<List<PeriodoUniversidadDTO>> listUniversityPeriod() {
        List<PeriodoUniversidadDTO> periods = periodoUniversidadService.findUniversityPeriodList();
        return new ResponseEntity<>(periods, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene las coordinaciones de la persona",
        description = """
            idPersona sale del JWT. Devuelve las coordinaciones activas de RVDIES.PERSONACOORDINACION.
            idPeriodoUniversidad es obligatorio. idConvocatoria, si se envía, debe pertenecer a ese periodo.
            """
    )
    @GetMapping("/list")
    public ResponseEntity<List<CoordinacionDTO>> listCoordinations(@RequestParam(required = true) Long idConvocatoria, @RequestParam Long idPeriodoUniversidad) {
        return ResponseEntity.ok(cargaService.findCoordinationsByPerson(idConvocatoria, idPeriodoUniversidad));
    }

    @Operation(
        summary = "Lista los tipos de vinculación de una convocatoria",
        description = """
            Une RVDIES.TIPOVINCULACIONCONVOCATORIA con RVDIES.TIPOVINCULACION por CONV_ID.
            El id de cada ítem es TIVC_ID. Incluye tipo de horas y si es de planta.
            """
    )
    @GetMapping("/list-linkage-types")
    public ResponseEntity<List<TipoVinculacionListaDTO>> listLinkageTypes(@RequestParam Long idConvocatoria) {
        return ResponseEntity.ok(cargaService.findLinkageTypesByCall(idConvocatoria));
    }

}
