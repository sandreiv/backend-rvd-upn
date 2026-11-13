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
 * 06/11/2026 - Búsqueda de docente de carrera vigente en trabajador labor.
 * 06/11/2026 - Alta de carga docente.
 * 09/11/2026 - Listado de docentes por tipo de vinculación de la convocatoria.
 * 09/11/2026 - Árbol de tipos de actividad desde RVDIES.TIPOACTIVIDADES.
 * 09/11/2026 - Restricciones de actividad por tipo de vinculación.
 * 05/11/2026 - Horas mínima y máxima del tipo de vinculación.
 * 11/11/2026 - Programas por unidad, coordinación y periodo con restricción de director.
 * 11/11/2026 - Pensum por programa y materias del pensum.
 * 11/11/2026 - Grupos de la materia en el periodo de la convocatoria y sus extensiones.
 * 12/11/2026 - Componentes de una materia electiva.
 * 12/11/2026 - Alta de detalles de actividad de la carga docente.
 * 12/11/2026 - Listado de detalle de carga docente.
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DistribucionActividadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HorasContratoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ContratoCalculoContext;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaComponenteBancoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PensumListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionActividadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadListadoDTO;
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
import org.springframework.http.HttpStatus;


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
            id es TIVI_ID e idTipoVinculacionConvocatoria es TIVC_ID.
            clasificacionDocente y dedicacionDocente incluyen id y descripcion, además de
            mínimos, máximos, forma de pago, tipo de contrato, si es de planta y tipo de horas.
            """
    )
    @GetMapping("/list-linkage-types")
    public ResponseEntity<TipoVinculacionListadoDTO> listLinkageTypes(@RequestParam Long idConvocatoria) {
        return ResponseEntity.ok(cargaService.findLinkageTypesByCall(idConvocatoria));
    }

    @Operation(
        summary = "Busca docentes para carga (no tiene en cuenta docentes carrera)",
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
        summary = "Obtiene el mínimo y el máximo de horas del tipo de vinculación",
        description = """
            Lee RVDIES.TIPOVINCULACION.TIVI_MINIMO y TIVI_MAXIMO según idTipoVinculacion.
            Los valores se devuelven tal como están guardados. Si el tipo no existe, responde 404.
            """
    )
    @GetMapping("/contract-hours")
    public ResponseEntity<HorasContratoDTO> getContractHours(@RequestParam Long idTipoVinculacion) {
        return ResponseEntity.ok(contratoCargaService.findContractHours(idTipoVinculacion));
    }


    // La implemnetación para el cargue SOLO DEBE HACER CÁLCULOS PARA TIPO SALARIO.
    // Para tipo CATEDRA este valor se va a calcular únicamente cuando ya se carguen todas las actividades. ----> Cantidad de horas != horas semanales máximas.
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


    @Operation(
        summary = "Busca docentes de carrera",
        description = """
            Busca en GENERAL.PERSONAGENERAL por documento y/o fragmento de nombre o apellido.
            Solo devuelve personas que existen en COMITES.ESCALAFON y en TALENTOV3.TRABAJADORLABOR
            con TRLA_FECHAFINAL nulo. Incluye categoría y puntos.
            anio es obligatorio: con RVD.PUNTOSVIGENCIA calcula valorPunto y la asignación salarial
            (puntos del escalafón × valor del punto). Si no hay vigencia para ese año, responde 404.
            """
    )
    @GetMapping("/search-career-professor")
    public ResponseEntity<List<BusquedaDocenteDTO>> searchCareerProfessor(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String documento,
            @RequestParam Long anio) {
        return ResponseEntity.ok(cargaService.searchCareerProfessor(nombre, documento, anio));
    }

    @Operation(
        summary = "Lista los docentes de un tipo de vinculación de la convocatoria",
        description = """
            Solo docentes con registro en RVDIES.CARGADOCENTE para ese TIVC_ID.
            Incluye persona, categoría, coordinación, el estado de RVDIES.ESTADOS,
            fechas, valores, horas,
            puntos, semanas y si tiene actividades en RVDIES.DETALLECARGADOCENTE.
            Si el tipo de vinculación no existe, responde 404.
            """
    )
    @GetMapping("/list-professors")
    public ResponseEntity<List<DocenteCargaDTO>> listProfessors(
            @RequestParam Long idTipoVinculacionConvocatoria) {
        return ResponseEntity.ok(cargaService.listProfessors(idTipoVinculacionConvocatoria));
    }

    @Operation(
        summary = "Agrega un docente a la carga",
        description = """
            Guarda en RVDIES.CARGADOCENTE. idTipoVinculacionConvocatoria (TIVC_ID) es obligatorio.
            ESTA_ID queda con el estado de RVDIES.ESTADOS cuyo ESTA_ORDEN es 1.
            El cuerpo admite persona, categoría, coordinación, fechas, valores, horas, puntos,
            semanas, novedad y observación. Lo que no venga queda nulo.
            Si la persona ya está en esa vinculación y coordinación, responde 409.
            """
    )
    @PostMapping("/add-professor")
    public ResponseEntity<Void> addProfessor(@RequestBody CargaDocenteFormularioDTO dto) {
        cargaService.addProfessor(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista los tipos de actividad en árbol",
        description = """
            Lee RVDIES.TIPOACTIVIDADES y devuelve solo los padres (TIAC_IDPADRE nulo).
            Cada nodo trae sus descendientes en hijos, ordenados por TIAC_ORDEN y nombre.
            Incluye estado, componente, docencia directa, dependencia y horas.
            """
    )
    @GetMapping("/list-activity-types")
    public ResponseEntity<List<TipoActividadListadoDTO>> listActivityTypes() {
        return ResponseEntity.ok(cargaService.listActivityTypes());
    }

    @Operation(
        summary = "Lista las restricciones de actividad de un tipo de vinculación",
        description = """
            Lee RVDIES.RESTRICCIONACTIVIDADES filtrado por TIVI_ID.
            Cada ítem trae el id de la restricción y el tipo de actividad (id y nombre).
            Si el tipo de vinculación no existe, responde 404.
            Si no tiene restricciones, responde una lista vacía.
            """
    )
    @GetMapping("/list-activity-restrictions")
    public ResponseEntity<List<RestriccionActividadListadoDTO>> listActivityRestrictions(
            @RequestParam Long idTipoVinculacion) {
        return ResponseEntity.ok(cargaService.listActivityRestrictions(idTipoVinculacion));
    }

    @Operation(
        summary = "Lista los programas de una coordinación en una unidad regional y un periodo",
        description = """
            Solo programas que existen en RVDIES.PROGRAMACOORDINACION para la coordinación
            y en ACADEMICO.UNIDADPROGRAMA de la unidad regional.
            idPersona sale del JWT y debe tener PERSONACOORDINACION activa en esa coordinación.
            Si el programa tiene restricción activa (RVDIES.RESTRICCIONPROGRAMAS.REPR_ESTADO = 1),
            solo lo ve el director indicado en esa restricción. Sin restricción activa, lo ve
            cualquier persona activa de la coordinación. Si no hay programas, responde lista vacía.
            """
    )
    @GetMapping("/list-program")
    public ResponseEntity<List<ProgramaDTO>> listPrograms(
            @RequestParam Long idCoordinacion,
            @RequestParam Long idUnidadRegional,
            @RequestParam Long idPeriodoUniversidad) {
        return ResponseEntity.ok(cargaService.listPrograms(idCoordinacion, idUnidadRegional, idPeriodoUniversidad));
    }

    @Operation(
        summary = "Lista los pensum de un programa",
        description = """
            Lee ACADEMICO.PENSUM filtrado por PROG_ID.
            Cada ítem trae id, descripción, año de inicio, periodo de inicio y número de periodos.
            Si el programa no existe, responde 404. Si no tiene pensum, responde una lista vacía.
            """
    )
    @GetMapping("/list-pensum")
    public ResponseEntity<List<PensumListadoDTO>> listPensums(@RequestParam Long idPrograma) {
        return ResponseEntity.ok(cargaService.listPensums(idPrograma));
    }

    @Operation(
        summary = "Lista las materias de un pensum",
        description = """
            Lee ACADEMICO.PENSUMMATERIA y ACADEMICO.MATERIA del pensum indicado.
            Excluye la materia si su código existe en RVDIES.MATERIAPARTICULAR para el programa del pensum.
            esElectiva es true cuando el código está en ACADEMICO.BANCODEMATERIAS.
            horasDirectas es la suma de horas prácticas y teóricas.
            tieneGrupo es true si la materia tiene un grupo activo en ACADEMICO.GRUPO.
            Si el pensum no existe, responde 404. Si no quedan materias, responde una lista vacía.
            """
    )
    @GetMapping("/list-subject")
    public ResponseEntity<List<MateriaDTO>> listSubjects(@RequestParam Long idPensum) {
        return ResponseEntity.ok(cargaService.listSubjects(idPensum));
    }

    @Operation(
        summary = "Lista los componentes de una materia electiva",
        description = """
            El código debe existir en ACADEMICO.BANCODEMATERIAS.
            Devuelve las filas de ACADEMICO.MATERIACOMPONENTEBANCO de ese banco.
            Cada ítem trae la submateria, el peso (MACB_PESO) y el estado (MACB_ACTIVA).
            Si el banco no existe, responde 404. Si no tiene componentes, responde una lista vacía.
            """
    )
    @GetMapping("/list-subject-components")
    public ResponseEntity<List<MateriaComponenteBancoDTO>> listSubjectComponents(
            @RequestParam String codigoMateria) {
        return ResponseEntity.ok(cargaService.listSubjectComponents(codigoMateria));
    }

    @Operation(
        summary = "Lista los grupos de una materia",
        description = """
            Lee ACADEMICO.GRUPO de la materia cuyo PEUN_ID es el periodo de RVDIES.CONVOCATORIA
            o uno de los periodos de RVDIES.PERIODOEXTENSION de esa convocatoria.
            Cada ítem trae id, nombre, capacidad y el periodo universitario del grupo.
            Si la convocatoria no existe, responde 404. Si no hay grupos, responde una lista vacía.
            """
    )
    @GetMapping("/list-subject-group")
    public ResponseEntity<List<GrupoDTO>> listSubjectGroups(
            @RequestParam Long idConvocatoria,
            @RequestParam String codigoMateria) {
        return ResponseEntity.ok(cargaService.listSubjectGroups(idConvocatoria, codigoMateria));
    }

    @Operation(
        summary = "Guarda la distribución de actividades de un docente",
        description = """
            Inserta cada detalle en RVDIES.DETALLECARGADOCENTE para la carga indicada.
            Guarda tipo de actividad, horas, coordinación, programa, grupo, materia,
            materia de banco, el centro de costo de la materia y el valor de extras en DECD_EXTRAS.
            extras es obligatorio y admite hasta 30 caracteres.
            idUnidadRegional viaja en el cuerpo y no se persiste: la tabla no tiene esa columna.
            Si la carga, el tipo de actividad, la coordinación, el programa o el grupo no existen, responde 404.
            """
    )
    @PostMapping("/save-detail-professor-load")
    public ResponseEntity<Void> saveDetailProfessorLoad(@RequestBody DistribucionActividadFormularioDTO dto) {
        cargaService.saveDetailProfessorPreload(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista el detalle de carga de un docente",
        description = """
            Lista los detalles de RVDIES.DETALLECARGADOCENTE de la carga indicada.
            Cada ítem trae el tipo de actividad guardado en TIAC_ID y su padre, la unidad del grupo,
            el programa y el pensum de ese programa. La materia del pensum es MATE_MATERIABANCO
            cuando la actividad es de banco; si no, es MATE_CODIGOMATERIA. También trae la materia,
            el componente de banco, el grupo, el centro de costo, las horas, el indicativo de si son horas extras
            y la ponderación académica de la materia y de la materia de banco.
            Si la carga no existe, responde 404. Si no tiene detalles, responde una lista vacía.
            """
    )
    @GetMapping("/list-detail-professor-load")
    public ResponseEntity<List<DetalleCargaDocenteDTO>> listDetailProfessorLoad(@RequestParam Long idCargaDocente) {
        return ResponseEntity.ok(cargaService.listDetailProfessorLoad(idCargaDocente));
    }

    @Operation(
        summary = "Lista las materias de práctica educativa de un pensum",
        description = """
            Lee ACADEMICO.PENSUMMATERIA y ACADEMICO.MATERIA del pensum indicado.
            Solo incluye la materia si existe en RVDIES.MATERIAPARTICULAR para el programa del pensum
            y MAPA_TIPOMATERIA es PRACTICAEDUCATIVA.
            La respuesta usa el mismo MateriaDTO del listado de materias del pensum,
            incluida la ponderación académica.
            Si el pensum no existe, responde 404. Si no hay materias de ese tipo, responde una lista vacía.
            """
    )
    @GetMapping("/list-educational-practice-subject")
    public ResponseEntity<List<MateriaDTO>> listEducationalPracticeSubject(@RequestParam Long idPensum) {
        return ResponseEntity.ok(cargaService.listEducationalPracticeSubject(idPensum));
    }

    
}
