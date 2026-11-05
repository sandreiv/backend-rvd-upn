/**
 * Aplicación: rvd
 * Archivo: CatedraContratoStrategy.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.contratacion.strategy
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.contratacion.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import co.edu.unipamplona.ciadti.rvd.model.dto.ContratoCalculoContext;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.enums.FormaPago;
import co.edu.unipamplona.ciadti.rvd.util.ValorContratacionCalculator;

@Service
public class CatedraContratoStrategy implements ContratoFormaPagoStrategy {

    @Override
    public FormaPago getFormaPago() {
        return FormaPago.CATEDRA;
    }

    @Override
    public ValorContratacionDTO calculate(ContratoCalculoContext context) {
        if (!canCalculate(context)) {
            return null;
        }
        return ValorContratacionCalculator.calculateCatedra(
                context.horasSemanales(),
                context.semanas(),
                context.valorHora(),
                context.fechaInicio(),
                context.fechaFin());
    }

    @Override
    public void apply(CargaDocenteEntity carga, ValorContratacionDTO valor) {
        if (valor == null) {
            carga.setValorContrato(null);
            carga.setValorPrestaciones(null);
            carga.setTotalContrato(null);
            carga.setSalario(null);
            return;
        }
        carga.setValorContrato(valor.valorContrato());
        carga.setValorPrestaciones(valor.totalPrestaciones());
        carga.setTotalContrato(valor.totalContrato());
        carga.setSalario(valor.salario());
    }

    private static boolean canCalculate(ContratoCalculoContext context) {
        return context.fechaInicio() != null
                && context.fechaFin() != null
                && context.valorHora() != null
                && isPositive(context.semanas())
                && isPositive(context.horasSemanales());
    }

    private static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
}
