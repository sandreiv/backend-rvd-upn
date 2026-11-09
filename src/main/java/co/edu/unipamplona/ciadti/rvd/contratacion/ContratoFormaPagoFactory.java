/**
 * Aplicación: rvd
 * Archivo: ContratoFormaPagoFactory.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.contratacion
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.contratacion;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import co.edu.unipamplona.ciadti.rvd.contratacion.strategy.ContratoFormaPagoStrategy;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.enums.FormaPago;

@Component
public class ContratoFormaPagoFactory {

    private final Map<FormaPago, ContratoFormaPagoStrategy> strategies;

    public ContratoFormaPagoFactory(Set<ContratoFormaPagoStrategy> strategies) {
        Map<FormaPago, ContratoFormaPagoStrategy> indexed = new EnumMap<>(FormaPago.class);
        for (ContratoFormaPagoStrategy strategy : strategies) {
            ContratoFormaPagoStrategy previous = indexed.put(strategy.getFormaPago(), strategy);
            if (previous != null) {
                throw new IllegalStateException(
                        "Hay más de una estrategia para la forma de pago " + strategy.getFormaPago());
            }
        }
        for (FormaPago formaPago : FormaPago.values()) {
            if (!indexed.containsKey(formaPago)) {
                throw new IllegalStateException(
                        "Falta la estrategia de contratación para " + formaPago);
            }
        }
        this.strategies = Map.copyOf(indexed);
    }

    public ContratoFormaPagoStrategy get(FormaPago formaPago) {
        if (formaPago == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La forma de pago es obligatoria.");
        }
        ContratoFormaPagoStrategy strategy = strategies.get(formaPago);
        if (strategy == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No hay fórmula para la forma de pago " + formaPago);
        }
        return strategy;
    }
}
