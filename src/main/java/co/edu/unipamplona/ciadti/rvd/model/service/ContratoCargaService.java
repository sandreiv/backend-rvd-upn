/**
 * Aplicación: rvd
 * Archivo: ContratoCargaService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Creación inicial
 * 05/11/2026 - Cálculo previo según tipo de vinculación.
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.ContratoCalculoContext;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.enums.FormaPago;

public interface ContratoCargaService {

    ValorContratacionDTO liquidar(CargaDocenteEntity carga);

    ValorContratacionDTO calculate(FormaPago formaPago, ContratoCalculoContext context);

    ValorContratacionDTO calculateByLinkageType(Long idTipoVinculacion, ContratoCalculoContext context);
}
