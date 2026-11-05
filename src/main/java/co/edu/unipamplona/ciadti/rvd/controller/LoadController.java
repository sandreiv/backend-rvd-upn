/**
 * Aplicación: rvd
 * Archivo: ProfessorPreloadController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 05/11/2026 - Forma de pago desde el tipo de vinculación en value-points-preload.
 * 05/11/2026 - Búsqueda de docente con valor del punto y asignación salarial.
 * 05/11/2026 - Cálculo de contrato según tipo de vinculación.
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unipamplona.ciadti.rvd.model.dto.ContratoCalculoContext;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.BusquedaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ContratoCargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.CargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.PeriodoUniversidadService;
import co.edu.unipamplona.ciadti.rvd.util.ParseUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/load")
public class LoadController {

    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;
    private final PeriodoUniversidadService periodoUniversidadService;
    private final CargaService cargaService;
    private final ContratoCargaService contratoCargaService;
    
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
            unidadAreaPadre es el área de la coordinación padre (COOR_IDPADRE).
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
            Une RVDIES.TIPOVINCULACIONCONVOCATORIA con RVDIES.TIPOVINCULACION por CONV_ID
            y trae ACADEMICO.CLASIFICACIONDOCENTE y ACADEMICO.DEDICACIONDOCENTE.
            La respuesta es TipoVinculacionListadoDTO. Cada ítem es TipoVinculacionesDTO:
            clasificacionDocente y dedicacionDocente incluyen id y descripcion, además de
            mínimos, máximos, forma de pago, tipo de contrato, si es de planta y tipo de horas.
            """
    )
    @GetMapping("/list-linkage-types")
    public ResponseEntity<TipoVinculacionListadoDTO> listLinkageTypes(@RequestParam Long idConvocatoria) {
        return ResponseEntity.ok(cargaService.findLinkageTypesByCall(idConvocatoria));
    }

    @Operation(
        summary = "Busca docentes para preasignacion",
        description = """
            Busca en GENERAL.PERSONAGENERAL por documento y/o fragmento de nombre o apellido.
            Solo devuelve personas que existen en COMITES.ESCALAFON e incluye categoría y puntos.
            anio es obligatorio: con RVD.PUNTOSVIGENCIA calcula valorPunto y la asignación salarial
            (puntos del escalafón × valor del punto). Si no hay vigencia para ese año, responde 404.
            """
    )
    @GetMapping("/search-professor")
    public ResponseEntity<List<BusquedaDocenteDTO>> searchProfessor(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String documento,
            @RequestParam Long anio) {
        return ResponseEntity.ok(cargaService.searchProfessor(nombre, documento, anio));
    }



    @Operation(
        summary = "Calcula el valor del contrato según el tipo de vinculación",
        description = """
            Lee RVDIES.TIPOVINCULACION.TIVI_FORMAPAGO y aplica la fórmula de esa forma de pago.
            SALARIO usa fechaInicio, fechaFin y asignacionSalarial. Si la asignación no viene,
            la calcula con puntos × valorPunto. 
            CATEDRA no usa asignación ni puntos del docente. Requiere horasSemanales, semanas
            y valorHora, además de las fechas. valorHora es el de la categoría (valor del punto
            × puntos de la categoría), no los puntos del docente.
            Las fechas van en formato yyyy-MM-dd. Si faltan datos de la fórmula, responde 400.
            Si el tipo de vinculación no existe, responde 404.
            """
    )
    @GetMapping("/contract-value")
    public ResponseEntity<ValorContratacionDTO> calculateContractValue(
            @RequestParam Long idTipoVinculacion,
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin,
            @RequestParam(required = false) BigDecimal asignacionSalarial,
            @RequestParam(required = false) String puntos,
            @RequestParam(required = false) BigDecimal valorPunto,
            @RequestParam(required = false) BigDecimal horasSemanales,
            @RequestParam(required = false) BigDecimal semanas,
            @RequestParam(required = false) BigDecimal valorHora) {
        ContratoCalculoContext context = new ContratoCalculoContext(
                horasSemanales,
                semanas,
                valorHora,
                asignacionSalarial,
                valorPunto,
                puntos,
                fechaInicio,
                fechaFin);
        return ResponseEntity.ok(contratoCargaService.calculateByLinkageType(idTipoVinculacion, context));
    }


}
