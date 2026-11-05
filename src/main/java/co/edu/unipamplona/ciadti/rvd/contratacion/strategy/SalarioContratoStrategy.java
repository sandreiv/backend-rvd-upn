/**
 * Aplicación: rvd
 * Archivo: SalarioContratoStrategy.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.contratacion.strategy
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Creación inicial
 * 05/11/2026 - Sin puntos no se calcula la asignación.
 */
package co.edu.unipamplona.ciadti.rvd.contratacion.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import co.edu.unipamplona.ciadti.rvd.model.dto.ContratoCalculoContext;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.enums.FormaPago;
import co.edu.unipamplona.ciadti.rvd.util.ValorContratacionCalculator;

@Service
public class SalarioContratoStrategy implements ContratoFormaPagoStrategy {

    private static final int ESCALA_MONETARIA = 2;

    @Override
    public FormaPago getFormaPago() {
        return FormaPago.SALARIO;
    }

    @Override
    public ValorContratacionDTO calculate(ContratoCalculoContext context) {
        if (context.fechaInicio() == null || context.fechaFin() == null) {
            return null;
        }
        BigDecimal asignacion = resolveAsignacion(context);
        if (asignacion == null) {
            return null;
        }
        return ValorContratacionCalculator.calculate(
                asignacion,
                context.fechaInicio(),
                context.fechaFin());
    }

    @Override
    public void apply(CargaDocenteEntity carga, ValorContratacionDTO valor) {
        if (valor == null) {
            return;
        }
        carga.setValorContrato(valor.valorContrato());
        carga.setValorPrestaciones(valor.totalPrestaciones());
        carga.setTotalContrato(valor.totalContrato());
    }

    private static BigDecimal resolveAsignacion(ContratoCalculoContext context) {
        if (context.salario() != null) {
            return context.salario();
        }
        if (context.valorPunto() == null) {
            return null;
        }
        BigDecimal puntos = resolvePuntos(context.puntos());
        if (puntos == null) {
            return null;
        }
        return puntos
                .multiply(context.valorPunto())
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
    }

    private static BigDecimal resolvePuntos(String puntos) {
        if (puntos == null || puntos.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(puntos.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
