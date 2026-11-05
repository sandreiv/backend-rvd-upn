/**
 * Aplicación: rvd
 * Archivo: ContratoFormaPagoStrategy.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.contratacion.strategy
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.contratacion.strategy;

import co.edu.unipamplona.ciadti.rvd.model.dto.ContratoCalculoContext;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.enums.FormaPago;

public interface ContratoFormaPagoStrategy {

    FormaPago getFormaPago();

    ValorContratacionDTO calculate(ContratoCalculoContext context);

    void apply(CargaDocenteEntity carga, ValorContratacionDTO valor);
}
