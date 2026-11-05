/**
 * Aplicación: rvd
 * Archivo: ParseUtils.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.util
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/09/2026
 * Modificaciones:
 * 10/09/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.util;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;

public final class ParseUtils {

    private ParseUtils() {
    }

    public static Long parseNullableLong(String value) {
        if (value == null || value.isBlank() || "null".equalsIgnoreCase(value.trim())) {
            return null;
        }
        return Long.valueOf(value.trim());
    }

    public static BigDecimal parseBigDecimal(String valor, String campo) {
        if (!StringUtils.hasText(valor)) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "El " + campo + " no esta definido");
        }
        try {
            return new BigDecimal(valor.trim());
        } catch (NumberFormatException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "El " + campo + " no es un valor numerico valido: " + valor);
        }
    }
    
}
/* 10/09/2026 @:Sebastian Jaimes */
