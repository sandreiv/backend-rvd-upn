/**
 * Aplicación: rvd
 * Archivo: ContratoCargaServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Creación inicial
 * 05/11/2026 - Cálculo previo según tipo de vinculación.
 * 05/11/2026 - Horas mínima y máxima del tipo de vinculación.
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.contratacion.ContratoFormaPagoFactory;
import co.edu.unipamplona.ciadti.rvd.contratacion.strategy.ContratoFormaPagoStrategy;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.ContratoCalculoContext;
import co.edu.unipamplona.ciadti.rvd.model.dto.HorasContratoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoVinculacionConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoVinculacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.enums.FormaPago;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoVinculacionConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoVinculacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TrabajadorLaborRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.ContratoCargaService;
import co.edu.unipamplona.ciadti.rvd.util.ResumenCargaAssembler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContratoCargaServiceImpl implements ContratoCargaService {

    private final ContratoFormaPagoFactory contratoFormaPagoFactory;
    private final TrabajadorLaborRepository trabajadorLaborRepository;
    private final TipoVinculacionConvocatoriaRepository tipoVinculacionConvocatoriaRepository;
    private final TipoVinculacionRepository tipoVinculacionRepository;

    @Override
    @Transactional
    public ValorContratacionDTO liquidar(CargaDocenteEntity carga) {
        if (carga == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga docente es obligatoria.");
        }
        if (isPlanta(carga.getIdPersonaGeneral())) {
            log.info("liquidar ===> Docente de planta. No se liquida contrato. idPersona={}",
                    carga.getIdPersonaGeneral());
            return null;
        }

        FormaPago formaPago = resolveFormaPago(carga);
        ContratoFormaPagoStrategy strategy = contratoFormaPagoFactory.get(formaPago);
        ValorContratacionDTO valor = strategy.calculate(toContext(carga));
        strategy.apply(carga, valor);
        log.info("liquidar ===> Contrato liquidado. idCarga={} formaPago={}", carga.getId(), formaPago);
        return valor;
    }

    @Override
    public ValorContratacionDTO calculate(FormaPago formaPago, ContratoCalculoContext context) {
        return contratoFormaPagoFactory.get(formaPago).calculate(context);
    }

    @Override
    @Transactional(readOnly = true)
    public ValorContratacionDTO calculateByLinkageType(Long idTipoVinculacion, ContratoCalculoContext context) {
        if (idTipoVinculacion == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de vinculación es obligatorio.");
        }
        if (context == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Los datos del cálculo son obligatorios.");
        }

        TipoVinculacionEntity tipoVinculacion = tipoVinculacionRepository.findById(idTipoVinculacion)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el tipo de vinculación."));
        FormaPago formaPago = FormaPago.from(tipoVinculacion.getFormaPago());


        ValorContratacionDTO valor = contratoFormaPagoFactory.get(formaPago).calculate(context);
        if (valor == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, missingInputsMessage(formaPago));
        }
        return valor;
    }

    @Override
    @Transactional(readOnly = true)
    public HorasContratoDTO findContractHours(Long idTipoVinculacion) {
        if (idTipoVinculacion == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de vinculación es obligatorio.");
        }
        TipoVinculacionEntity tipoVinculacion = tipoVinculacionRepository.findById(idTipoVinculacion)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el tipo de vinculación."));
        log.info("findContractHours ===> Consultando horas del tipo de vinculación. idTipoVinculacion={}",
                idTipoVinculacion);
        return new HorasContratoDTO(tipoVinculacion.getMinimo(), tipoVinculacion.getMaximo());
    }

    private static String missingInputsMessage(FormaPago formaPago) {
        if (formaPago == FormaPago.CATEDRA) {
            return "La cátedra requiere fecha inicio, fecha fin, horas semanales, semanas y valor hora.";
        }
        return "El salario requiere fecha inicio, fecha fin y asignación salarial, o puntos del docente y valor del punto. Sin puntos no se calcula.";
    }

    private boolean isPlanta(Long idPersonaGeneral) {
        if (idPersonaGeneral == null) {
            return false;
        }
        return trabajadorLaborRepository.existsVigenteByIdPersonaGeneral(idPersonaGeneral);
    }

    private FormaPago resolveFormaPago(CargaDocenteEntity carga) {
        if (carga.getIdTipoVinculacionConvocatoria() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene tipo de vinculación.");
        }
        TipoVinculacionConvocatoriaEntity relacion = tipoVinculacionConvocatoriaRepository
                .findById(carga.getIdTipoVinculacionConvocatoria())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el tipo de vinculación de la convocatoria."));
        TipoVinculacionEntity tipoVinculacion = tipoVinculacionRepository
                .findById(relacion.getIdTipoVinculacion())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el tipo de vinculación."));
        return FormaPago.from(tipoVinculacion.getFormaPago());
    }

    private static ContratoCalculoContext toContext(CargaDocenteEntity carga) {
        return new ContratoCalculoContext(
                ResumenCargaAssembler.parseHoras(carga.getHoras()),
                ResumenCargaAssembler.parseHoras(carga.getSemanas()),
                carga.getValorHora(),
                carga.getSalario(),
                carga.getValorPunto(),
                carga.getPuntos(),
                toLocalDate(carga.getFechaInicio()),
                toLocalDate(carga.getFechaFin()));
    }

    private static LocalDate toLocalDate(Date fecha) {
        if (fecha == null) {
            return null;
        }
        if (fecha instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
