/**
 * Aplicación: rvd
 * Archivo: ProfessorPreloadController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 25/08/2026 - Sebastian Jaimes - Listado coordinaciones por JWT (Coordinador/Decano)
 * 27/08/2026 - Horas de actividades por carga
 * 31/08/2026 - Sebastian Jaimes - Columna V. Hora en reporte Excel
 * 31/08/2026 - Sebastian Jaimes - Reporte PDF de preasignación
 * 04/09/2026 - Exclusion de once meses heredados en segundo periodo
 * 10/09/2026 - Sebastian Jaimes - Precarga activas solo preasignación
 * 10/09/2026 - Sebastian Jaimes - parseNullableLong a ParseUtils
 * 16/09/2026 - Listado de modalidades de contratación
 * 16/09/2026 - Novedad cambio de modalidad y horas
 * 18/09/2026 - Presupuesto efectivo de carga en novedades
 * 18/09/2026 - save-contract-modality-professor inserta fotografía
 * 18/09/2026 - CARG_VALOR al aprobar novedad; autorizado en preasignación
 * 21/09/2026 - Listado desarrollo: Aprobado Decano o Aval con novedad
 * 22/09/2026 - Novedad de actividades: FAD, CTEI e ISU
 * 29/09/2026 - Resumen de carga con novedad vigente
 * 29/09/2026 - Historial de novedades en el resumen
 * 06/10/2026 - PDF comparativo de novedad por CADO_ID
 * 15/10/2026 - Andrés Hernández - Las novedades ahora se relacionan a la modalidad de contratación
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
    public ResponseEntity<List<CoordinacionDTO>> listCoordinations(@RequestParam(required = false) Long idConvocatoria, @RequestParam Long idPeriodoUniversidad) {
        return ResponseEntity.ok(cargaService.findCoordinationsByPerson(idConvocatoria, idPeriodoUniversidad));
    }

}
