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
 * 17/11/2026 - Indicativo de docente titular en el detalle de la carga.
 * 17/11/2026 - Materias de la carga con programa y centro de costo.
 * 17/11/2026 - Borrado de los detalles de una carga docente.
 * 17/11/2026 - Proyectos de la persona de la carga docente.
 * 19/11/2026 - Centros de costo de la coordinación y de sus programas.
 * 19/11/2026 - Listado de todos los centros de costo.
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DistribucionActividadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminacionMasivaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HorasContratoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ContratoCalculoContext;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionCentrosCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HorarioClaseDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaComponenteBancoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaProgramaCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PensumListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoDTO;
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
            Excluye la materia si está en RVDIES.MATERIAPARTICULAR para el programa del pensum
            con un tipo distinto de MATERIACOMPARTIDA. Las materias compartidas sí se listan.
            esElectiva es true cuando el código está en ACADEMICO.BANCODEMATERIAS.
            horasDirectas es la suma de horas prácticas y teóricas.
            tieneGrupo es true si la materia tiene un grupo activo en ACADEMICO.GRUPO.
            esCompartida es true si el código está en RVDIES.MATERIAPARTICULAR del programa
            con tipo MATERIACOMPARTIDA. docentesPorGrupo sale de MAPA_DOCENTESXGRUPO
            y solo tiene valor en esas materias compartidas.
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
        summary = "Lista el horario de clases de un grupo",
        description = """
            Lee ACADEMICO.CLASESEMANAL del grupo indicado.
            Cada clase trae el día, las fechas y los bloques de ACADEMICO.BLOQUEHORAS
            de BLHO_IDINICIAL y BLHO_IDFINAL, con nombre y hora de inicio y final.
            Si el grupo no existe, responde 404. Si no tiene clases, responde una lista vacía.
            """
    )
    @GetMapping("/list-group-schedule")
    public ResponseEntity<List<HorarioClaseDTO>> listGroupSchedule(@RequestParam Long idGrupo) {
        return ResponseEntity.ok(cargaService.listGroupSchedule(idGrupo));
    }

    @Operation(
        summary = "Guarda la distribución de actividades de un docente",
        description = """
            Inserta cada detalle en RVDIES.DETALLECARGADOCENTE para la carga indicada.
            Guarda tipo de actividad, horas, coordinación, programa, grupo, materia,
            materia de banco, el centro de costo de la materia, extras en DECD_EXTRAS
            y, si viene, data en DECD_DATA.
            extras es obligatorio y admite hasta 30 caracteres.
            data es el JSON de atributos del detalle. En una materia compartida incluye
            idsClaseSemanal con los identificadores de las clases semanales elegidas.
            Si el detalle tiene grupo y esta carga es la del detalle más antiguo de ese grupo
            en la convocatoria, el backend agrega tipoDocente con valor TITULAR.
            Un tipoDocente enviado por el cliente se ignora.
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
        summary = "Elimina un detalle de carga docente",
        description = """
            Elimina un solo registro de RVDIES.DETALLECARGADOCENTE por DECD_ID.
            Los demás detalles de la carga permanecen.
            Si el detalle no existe, responde 404.
            """
    )
    @DeleteMapping("/delete-detail-professor-load")
    public ResponseEntity<Void> deleteDetailProfessorLoad(@RequestParam Long idDetalleCargaDocente) {
        cargaService.deleteDetailProfessorLoad(idDetalleCargaDocente);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina varios detalles de carga docente",
        description = """
            Elimina los registros de RVDIES.DETALLECARGADOCENTE cuyos DECD_ID llegan en ids.
            Acepta uno o varios. Un id repetido se elimina una sola vez.
            Si la lista está vacía o trae un id nulo, responde 400.
            Si algún detalle no existe, no elimina ninguno y responde 404.
            """
    )
    @PostMapping("/delete-detail-professor-load-bulk")
    public ResponseEntity<Void> deleteDetailProfessorLoadBulk(@RequestBody EliminacionMasivaDTO dto) {
        cargaService.deleteDetailProfessorLoadBulk(dto == null ? null : dto.ids());
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista el detalle de carga de un docente",
        description = """
            Lista los detalles de RVDIES.DETALLECARGADOCENTE de la carga indicada.
            Cada ítem trae el tipo de actividad guardado en TIAC_ID y su padre, la unidad del grupo,
            el programa y el pensum de ese programa. La materia del pensum es MATE_MATERIABANCO
            cuando la actividad es de banco; si no, es MATE_CODIGOMATERIA. También trae la materia,
            el componente de banco, el grupo, el centro de costo, las horas, el indicativo de si son horas extras,
            la ponderación académica de la materia y de la materia de banco, y las clases semanales
            guardadas en DECD_DATA, con su día, fechas y bloques de hora inicial y final.
            Si el detalle guardó tipoDocente TITULAR, la actividad lo devuelve en tipoDocente.
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


    @Operation(
        summary = "Lista las materias de una carga docente con su programa",
        description = """
            Lee las materias guardadas en RVDIES.DETALLECARGADOCENTE de la carga indicada.
            Cada ítem trae la materia, el programa del detalle, el centro de costo de ese programa
            en RVDIES.PROGRAMACOORDINACION de la coordinación de la carga y el grupo de ACADEMICO.GRUPO.
            Si la materia está en varios grupos, hay una fila por grupo.
            Si el detalle no tiene grupo, grupo viene nulo.
            Si la carga no existe, responde 404. Si no tiene materias con programa, responde una lista vacía.
            """
    )
    @GetMapping("/list-professor-subjects")
    public ResponseEntity<List<MateriaProgramaCargaDTO>> listProfessorSubjects(
            @RequestParam Long idCargaDocente) {
        return ResponseEntity.ok(cargaService.listProfessorSubjects(idCargaDocente));
    }

    @Operation(
        summary = "Lista los proyectos de la persona de una carga docente",
        description = """
            Toma el PEGE_ID de RVDIES.CARGADOCENTE y lista RVDIES.PERSONAPROYECTO de esa persona.
            Cada ítem trae horas, observación, programa, centro de costo y el proyecto
            de RVDIES.PROYECTOS con su padre, el tipo de actividad, la coordinación, la materia particular
            (materia, programa y centro de costo), el grupo, el acta, el código y el estado activo.
            PEPR_DATA se devuelve desglosado en funcion, nivel y horario.
            Si la carga no existe, responde 404. Si no tiene persona, responde 400.
            Si la persona no tiene proyectos, responde una lista vacía.
            """
    )
    @GetMapping("/list-professor-projects")
    public ResponseEntity<List<PersonaProyectoDTO>> listProfessorProjects(@RequestParam Long idCargaDocente) {
        return ResponseEntity.ok(cargaService.listProfessorProjects(idCargaDocente));
    }

    // Estos también se utilizan en la configuración de los proyectos
    @Operation(
        summary = "Lista los centros de costo de una coordinación y de sus programas",
        description = """
            Recibe el id de la coordinación.
            nombre es el de ACADEMICO.UNIDAD de UNID_IDAREA.
            centrosCosto sale de RVDIES.CENTROCOSTOCOORDINACION.
            programas agrupa RVDIES.PROGRAMACOORDINACION: cada programa trae los centros de costo
            de CONTABLEV3.CENTROCOSTO asociados a esa coordinación.
            Si la coordinación no existe, responde 404.
            Si no tiene centros de costo o programas, esas listas van vacías.
            """
    )
    @GetMapping("/list-coordination-cost-centers")
    public ResponseEntity<CoordinacionCentrosCostoDTO> listCoordinationCostCenters(
            @RequestParam Long idCoordinacion) {
        return ResponseEntity.ok(cargaService.listCoordinationCostCenters(idCoordinacion));
    }

    @Operation(
        summary = "Lista todos los centros de costo",
        description = """
            Lee todas las filas de CONTABLEV3.CENTROCOSTO.
            Cada ítem trae el id y la descripción.
            Si la tabla no tiene filas, responde una lista vacía.
            """
    )
    @GetMapping("/list-cost-centers")
    public ResponseEntity<List<CentroCostoDTO>> listCostCenters() {
        return ResponseEntity.ok(cargaService.listCostCenters());
    }
}
