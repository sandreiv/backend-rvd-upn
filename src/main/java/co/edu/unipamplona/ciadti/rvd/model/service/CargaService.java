/**
 * Aplicación: rvd
 * Archivo: CargaService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 05/11/2026 - Forma de pago desde el tipo de vinculación.
 * 05/11/2026 - Búsqueda de docente con valor del punto y asignación salarial.
 * 06/11/2026 - Búsqueda de docente de carrera vigente en trabajador labor.
 * 06/11/2026 - Alta de carga docente.
 * 09/11/2026 - Listado de docentes por tipo de vinculación de la convocatoria.
 * 09/11/2026 - Árbol de tipos de actividad desde RVDIES.TIPOACTIVIDADES.
 * 09/11/2026 - Restricciones de actividad por tipo de vinculación.
 * 11/11/2026 - Programas por unidad, coordinación y periodo con restricción de director.
 * 11/11/2026 - Pensum por programa y materias del pensum.
 * 11/11/2026 - Grupos de la materia en el periodo de la convocatoria y sus extensiones.
 * 12/11/2026 - Componentes de una materia electiva.
 * 12/11/2026 - Alta de detalles de actividad de la carga docente.
 * 12/11/2026 - Listado de detalle de carga docente.
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;


import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DistribucionActividadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionActividadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoVinculacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.BusquedaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaComponenteBancoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PensumListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosCargaDTO;


public interface CargaService {

    List<CoordinacionDTO> findCoordinationsByPerson(Long idConvocatoria, Long idPeriodoUniversidad);

    TipoVinculacionListadoDTO findLinkageTypesByCall(Long idConvocatoria);

    List<BusquedaDocenteDTO> searchProfessor(String nombre, String documento, Long anio);

    List<BusquedaDocenteDTO> searchCareerProfessor(String nombre, String documento, Long anio);

    List<DocenteCargaDTO> listProfessors(Long idTipoVinculacionConvocatoria);

    List<TipoActividadListadoDTO> listActivityTypes();

    List<RestriccionActividadListadoDTO> listActivityRestrictions(Long idTipoVinculacion);

    List<ProgramaDTO> listPrograms(Long idCoordinacion, Long idUnidadRegional, Long idPeriodoUniversidad);

    List<PensumListadoDTO> listPensums(Long idPrograma);

    List<MateriaDTO> listSubjects(Long idPensum);

    List<MateriaDTO> listEducationalPracticeSubject(Long idPensum);

    List<MateriaComponenteBancoDTO> listSubjectComponents(String codigoMateria);

    List<GrupoDTO> listSubjectGroups(Long idConvocatoria, String codigoMateria);

    void saveDetailProfessorPreload(DistribucionActividadFormularioDTO dto);

    List<DetalleCargaDocenteDTO> listDetailProfessorLoad(Long idCargaDocente);

    void addProfessor(CargaDocenteFormularioDTO dto);

    ValorPuntosCargaDTO getValuePointsPreload(
            Long anio,
            Long idCategoriaCatedratico,
            Long idPersona,
            Long idModalidadContratacion,
            Long idTipoVinculacion);


    
}
