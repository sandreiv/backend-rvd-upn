/**
 * Aplicación: rvd
 * Archivo: CargaServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 18/08/2026 - Andrés Hernández - Implementación de historial de estados en la carga docente
 * 25/08/2026 - Andrés Hernández - Avalar inscripción de la precarga
 * 25/08/2026 - Andrés Hernández - Rechazo de carga por parte de decano
 * 25/08/2026 - Sebastian Jaimes - Listado coordinaciones por JWT (Coordinador/Decano)
 * 25/08/2026 - Sebastian Jaimes - registradoPor con idPersona, acción e IP
 * 27/08/2026 - Andrés Hernández - Consulta de observaciones en la precarga
 * 27/08/2026 - Andrés Hernández - Unificación en el rechazo de carga
 * 27/08/2026 - Horas de actividades por carga
 * 28/08/2026 - Andrés Hernández - Marcar observaciones como vistas
 * 03/09/2026 - Andrés Hernández - Consulta de solicitudes CDP (Desarrollo académico)
 * 04/09/2026 - Docentes once meses heredados con estado pendiente
 * 07/09/2026 - Sebastian Jaimes - Listado coordinaciones académicas hijas
 * 07/09/2026 - Sebastian Jaimes - Coordinaciones con carga y docentes a verificar
 * 07/09/2026 - Sebastian Jaimes - Listado docentes por periodo, convocatoria y coordinación
 * 07/09/2026 - Sebastian Jaimes - Pendientes de verificación para header
 * 08/09/2026 - Andrés Hernández - Desaprobación de docentes en el preregistro
 * 08/09/2026 - Andrés Hernández - Aprobación de docentes verificados
 * 09/09/2026 - Andrés Hernández - Consulta de CDP para roles académicos
 * 10/09/2026 - Sebastian Jaimes - Coordinaciones contratación (preasignación + Aval Desarrollo)
 * 10/09/2026 - Sebastian Jaimes - Docentes aprobados para contratación
 * 11/09/2026 - Andrés Hernández - Consulta de novedades de actualización
 * 16/09/2026 - Andrés Hernández - Método que devuelve la lista de profesores para la sección de novedades
 * 16/09/2026 - Andrés Hernández - Consulta de los docentes para la sección de novedades
 * 17/09/2026 - Sebastian Jaimes - Valor de contrato con rango de fechas inclusivo
 * 17/09/2026 - Andrés Hernández - Consulta de detalles de actividades para novedades
 * 18/09/2026 - Sebastian Jaimes - Contrato cátedra con horas de actividades
 * 18/09/2026 - Sebastian Jaimes - Salario cátedra como PSM mensual
 * 18/09/2026 - Sebastian Jaimes - CARG_VALOR y autorizado en preasignación
 * 21/09/2026 - Listado desarrollo: Aprobado Decano o Aval con novedad
 * 28/09/2026 - Andrés Hernández - Novedad agregar docente
 * 29/09/2026 - Resumen de horas y centros con ResumenCargaAssembler
 * 30/09/2026 - Andrés Hernández - No tomar en cuenta docentes agregados como novedad para la preasignación
 * 07/10/2026 - Andrés Hernández - Manejo de docentes efectivos para las novedades y la contratación
 * 07/10/2026 - Andrés Hernández - Desasociar un proyecto en la preasignación borra su relacionCargaProyecto
 * 07/10/2026 - Andrés Hernández - Información del docente para contratación
 * 08/10/2026 - Sebastian Jaimes - Información del docente con actividades PTD
 * 13/10/2026 - Andrés Hernández - Consulta de las actividades para mostrar en el PTD
 * 15/10/2026 - Andrés Hernández - Las novedades ahora se relacionan a la modalidad de contratación
 * 21/10/2026 - Andrés Hernández - Busqueda de coordinaciones de contratación permiten revisión por Talento Humano
 * 23/10/2026 - Andrés Hernández - Vice academica ahora consulta solicitudes en 'VICERRECTORIA ACADEMICA' y 'CDP APROBADO'.
 * La información del CDP es del primer registro que encuentre con ese numero
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.CoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.CargaService;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CargaServiceImpl implements CargaService {

    private static final String ROL_COORDINADOR = "Coordinador";
    private static final String ROL_DECANO = "Decano";
    private static final String ROL_DESARROLLO = "Desarrollo academico";
    private static final String ROL_VICERRECTORIA = "Vicerrectoria academica";
    private static final String ROL_TALENTO = "Talento Humano";


    private final CoordinacionRepository coordinacionRepository;

    private final PersonaCoordinacionRepository personaCoordinacionRepository;

    private final ConvocatoriaRepository convocatoriaRepository;

    private final CoordinacionMapper coordinacionMapper;


    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionDTO> findCoordinationsByPerson(Long idConvocatoria, Long idPeriodoUniversidad) {
        if (idPeriodoUniversidad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio");
        }
        Long idPersona;
        try {
            idPersona = SecurityUtils.requireIdPersona();
        } catch (IllegalStateException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        if (idConvocatoria != null
                && !convocatoriaRepository.existsByIdAndIdPeriodoUniversidad(idConvocatoria, idPeriodoUniversidad)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La convocatoria no pertenece al periodo indicado");
        }
    
        return coordinacionMapper.toDtoList(
                personaCoordinacionRepository.findActiveByPersonAndCall(
                        idPersona, idPeriodoUniversidad, idConvocatoria));
    }



    private AuthUserDetails requireListadoUser() {
        AuthUserDetails user;
        try {
            user = SecurityUtils.requireUser();
            System.out.println("[DEBUG] USER: " + user);
        } catch (IllegalStateException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        if (user.getIdPersonaGeneral() == null) {
            throw new ApiException(HttpStatus.FORBIDDEN, "El token no trae idPersona");
        }
        if (!hasRole(user, ROL_COORDINADOR) && !hasRole(user, ROL_DECANO) && !hasRole(user, ROL_DESARROLLO) && !hasRole(user, ROL_VICERRECTORIA) && !hasRole(user, ROL_TALENTO)) {
            throw new ApiException(HttpStatus.FORBIDDEN,"El listado de coordinaciones requiere rol Coordinador, Decano, Desarrollo academico, Vicerrectoria academica o Talento Humano");
        }
        return user;
    }

    private void validateListadoFiltros(Long idConvocatoria, Long idPeriodoUniversidad) {
        if (idConvocatoria != null && idPeriodoUniversidad != null) {
            validateConvocatoriaBelongsToPeriod(idConvocatoria, idPeriodoUniversidad);
        }
        if (idConvocatoria == null && idPeriodoUniversidad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio cuando no se envía convocatoria");
        }
    }

    private List<CoordinacionListadoProjection> listForCoordinador(
            Long idConvocatoria,
            Long idPeriodoUniversidad,
            Long idPersona) {
        if (idConvocatoria != null) {
            System.out.println("[DEBUG] LISTANDO COMO COORDINADOR");
            System.out.println("[DEBUG] idConvocatoria: " + idConvocatoria);
            System.out.println("[DEBUG] idPersona: " + idPersona);
            return coordinacionRepository.findByConvocatoriaWithCarga(idConvocatoria, idPersona);
        }
        System.out.println("[DEBUG] LISTANDO SIN CONVOCATORIA");
        System.out.println("[DEBUG] idPeriodoUniversidad: " + idPeriodoUniversidad);
        System.out.println("[DEBUG] idPersona: " + idPersona);
        return coordinacionRepository.findWithoutCarga(idPersona, idPeriodoUniversidad);
    }


    private static boolean hasRole(AuthUserDetails user, String role) {
        return user.getRoles().stream()
                .filter(Objects::nonNull)
                .anyMatch(item -> role.equalsIgnoreCase(item.trim()));
    }

    private void validateConvocatoriaBelongsToPeriod(
            Long idConvocatoria,
            Long idPeriodoUniversidad) {
        var periodo = convocatoriaRepository.findPeriodoEntityByConvocatoriaId(idConvocatoria);
        if (periodo == null || !Objects.equals(periodo.getId(), idPeriodoUniversidad)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La convocatoria no pertenece al periodo universitario indicado");
        }
    }


}
