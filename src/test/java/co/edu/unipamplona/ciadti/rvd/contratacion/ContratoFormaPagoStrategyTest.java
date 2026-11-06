/**
 * Aplicación: rvd
 * Archivo: ContratoFormaPagoStrategyTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.contratacion
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.contratacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.unipamplona.ciadti.rvd.contratacion.strategy.CatedraContratoStrategy;
import co.edu.unipamplona.ciadti.rvd.contratacion.strategy.ContratoFormaPagoStrategy;
import co.edu.unipamplona.ciadti.rvd.contratacion.strategy.SalarioContratoStrategy;
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
import co.edu.unipamplona.ciadti.rvd.model.service.impl.ContratoCargaServiceImpl;

@ExtendWith(MockitoExtension.class)
class ContratoFormaPagoStrategyTest {

    private static final LocalDate INICIO = LocalDate.of(2026, 2, 1);
    private static final LocalDate FIN = LocalDate.of(2026, 6, 30);

    private ContratoFormaPagoFactory factory;
    private ContratoCargaServiceImpl service;

    @Mock
    private TrabajadorLaborRepository trabajadorLaborRepository;

    @Mock
    private TipoVinculacionConvocatoriaRepository tipoVinculacionConvocatoriaRepository;

    @Mock
    private TipoVinculacionRepository tipoVinculacionRepository;

    @BeforeEach
    void setUp() {
        Set<ContratoFormaPagoStrategy> strategies = Set.of(
                new CatedraContratoStrategy(),
                new SalarioContratoStrategy());
        factory = new ContratoFormaPagoFactory(strategies);
        service = new ContratoCargaServiceImpl(
                factory,
                trabajadorLaborRepository,
                tipoVinculacionConvocatoriaRepository,
                tipoVinculacionRepository);
    }

    @Test
    void catedraIncluyePrestacionesYGuardaElPromedioMensual() {
        ContratoCalculoContext context = new ContratoCalculoContext(
                new BigDecimal("8"),
                new BigDecimal("16"),
                new BigDecimal("15000"),
                null,
                null,
                null,
                INICIO,
                FIN);

        ValorContratacionDTO valor = service.calculate(FormaPago.CATEDRA, context);

        assertMoney("1920000.00", valor.valorContrato());
        assertMoney("384000.00", valor.salario());
        assertMoney("160000.00", valor.valorCesantias());
        assertMoney("8000.00", valor.valorIntereses());
        assertMoney("160000.00", valor.valorPrimaLegal());
        assertMoney("80000.00", valor.valorVacaciones());
        assertMoney("408000.00", valor.totalPrestaciones());
        assertMoney("2328000.00", valor.totalContrato());
    }

    @Test
    void salarioUsaLaAsignacionYConservaEseSalario() {
        ContratoCalculoContext context = new ContratoCalculoContext(
                null,
                null,
                null,
                new BigDecimal("4000000"),
                null,
                null,
                INICIO,
                FIN);

        ValorContratacionDTO valor = service.calculate(FormaPago.SALARIO, context);
        CargaDocenteEntity carga = new CargaDocenteEntity();
        carga.setSalario(new BigDecimal("4000000"));
        factory.get(FormaPago.SALARIO).apply(carga, valor);

        assertMoney("20000000.00", valor.valorContrato());
        assertMoney("1666666.67", valor.valorCesantias());
        assertMoney("83333.33", valor.valorIntereses());
        assertMoney("1666666.67", valor.valorPrimaLegal());
        assertMoney("833333.33", valor.valorVacaciones());
        assertMoney("4250000.00", valor.totalPrestaciones());
        assertMoney("24250000.00", valor.totalContrato());
        assertMoney("4000000.00", valor.salario());
        assertMoney("4000000.00", carga.getSalario());
        assertMoney("20000000.00", carga.getValorContrato());
    }

    @Test
    void salarioSinPuntosNoCalculaLaAsignacion() {
        ContratoCalculoContext context = new ContratoCalculoContext(
                null,
                null,
                null,
                null,
                new BigDecimal("10000"),
                "abc",
                INICIO,
                FIN);

        ValorContratacionDTO valor = service.calculate(FormaPago.SALARIO, context);

        assertNull(valor);
    }

    @Test
    void catedraSinHorasNoLiquidaYLimpiaElContrato() {
        CargaDocenteEntity carga = new CargaDocenteEntity();
        carga.setSalario(new BigDecimal("384000"));
        carga.setValorContrato(new BigDecimal("1"));

        ValorContratacionDTO valor = factory.get(FormaPago.CATEDRA).calculate(
                new ContratoCalculoContext(
                        BigDecimal.ZERO,
                        new BigDecimal("16"),
                        new BigDecimal("15000"),
                        null,
                        null,
                        null,
                        INICIO,
                        FIN));
        factory.get(FormaPago.CATEDRA).apply(carga, valor);

        assertNull(valor);
        assertNull(carga.getValorContrato());
        assertNull(carga.getSalario());
    }

    @Test
    void plantaNoEntraALaFormula() {
        org.mockito.Mockito.when(trabajadorLaborRepository.existsVigenteByIdPersonaGeneral(10L))
                .thenReturn(true);
        CargaDocenteEntity carga = new CargaDocenteEntity();
        carga.setIdPersonaGeneral(10L);
        carga.setSalario(new BigDecimal("4000000"));
        carga.setValorContrato(new BigDecimal("99"));

        ValorContratacionDTO valor = service.liquidar(carga);

        assertNull(valor);
        assertMoney("99", carga.getValorContrato());
        assertMoney("4000000.00", carga.getSalario());
    }

    @Test
    void liquidarSalarioLeeLaFormaDePagoDelTipoDeVinculacion() {
        TipoVinculacionConvocatoriaEntity relacion = new TipoVinculacionConvocatoriaEntity();
        relacion.setIdTipoVinculacion(5L);
        TipoVinculacionEntity tipo = new TipoVinculacionEntity();
        tipo.setFormaPago("salario");

        org.mockito.Mockito.when(trabajadorLaborRepository.existsVigenteByIdPersonaGeneral(10L))
                .thenReturn(false);
        org.mockito.Mockito.when(tipoVinculacionConvocatoriaRepository.findById(7L))
                .thenReturn(Optional.of(relacion));
        org.mockito.Mockito.when(tipoVinculacionRepository.findById(5L))
                .thenReturn(Optional.of(tipo));

        CargaDocenteEntity carga = new CargaDocenteEntity();
        carga.setId(3L);
        carga.setIdPersonaGeneral(10L);
        carga.setIdTipoVinculacionConvocatoria(7L);
        carga.setSalario(new BigDecimal("4000000"));
        carga.setFechaInicio(toDate(INICIO));
        carga.setFechaFin(toDate(FIN));

        ValorContratacionDTO valor = service.liquidar(carga);

        assertMoney("24250000.00", valor.totalContrato());
        assertMoney("4000000.00", carga.getSalario());
        assertMoney("4250000.00", carga.getValorPrestaciones());
    }

    @Test
    void calculateByLinkageTypeUsaLaFormaDePagoDelTipo() {
        TipoVinculacionEntity tipo = new TipoVinculacionEntity();
        tipo.setFormaPago("CATEDRA");
        org.mockito.Mockito.when(tipoVinculacionRepository.findById(5L))
                .thenReturn(Optional.of(tipo));

        ValorContratacionDTO valor = service.calculateByLinkageType(5L, new ContratoCalculoContext(
                new BigDecimal("8"),
                new BigDecimal("16"),
                new BigDecimal("15000"),
                null,
                null,
                null,
                INICIO,
                FIN));

        assertMoney("2328000.00", valor.totalContrato());
    }

    @Test
    void findContractHoursDevuelveMinimoYMaximoDelTipo() {
        TipoVinculacionEntity tipo = new TipoVinculacionEntity();
        tipo.setMinimo("8");
        tipo.setMaximo("12");
        org.mockito.Mockito.when(tipoVinculacionRepository.findById(5L))
                .thenReturn(Optional.of(tipo));

        HorasContratoDTO horas = service.findContractHours(5L);

        assertEquals("8", horas.minimo());
        assertEquals("12", horas.maximo());
    }

    @Test
    void calculateByLinkageTypeCatedraSinHorasRespondeSolicitudInvalida() {
        TipoVinculacionEntity tipo = new TipoVinculacionEntity();
        tipo.setFormaPago("catedra");
        org.mockito.Mockito.when(tipoVinculacionRepository.findById(5L))
                .thenReturn(Optional.of(tipo));

        ApiException ex = assertThrows(ApiException.class, () -> service.calculateByLinkageType(
                5L,
                new ContratoCalculoContext(null, null, null, null, null, null, INICIO, FIN)));

        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST, ex.getStatus());
        assertTrue(ex.getMessage().contains("cátedra"));
    }

    @Test
    void rechazaUnaFormaDePagoDistintaDeCatedraYSalario() {
        ApiException ex = assertThrows(ApiException.class, () -> FormaPago.from("PLANTA"));
        assertTrue(ex.getMessage().contains("PLANTA"));
    }

    @Test
    void laFactoriaExigeLasDosEstrategias() {
        assertThrows(
                IllegalStateException.class,
                () -> new ContratoFormaPagoFactory(Set.of(new CatedraContratoStrategy())));
    }

    private static Date toDate(LocalDate fecha) {
        return Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static void assertMoney(String expected, BigDecimal actual) {
        assertEquals(
                0,
                new BigDecimal(expected).compareTo(actual),
                () -> "esperado " + expected + " pero fue " + actual);
    }
}
