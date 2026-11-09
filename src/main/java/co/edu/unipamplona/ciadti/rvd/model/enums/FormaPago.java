/**
 * Aplicación: rvd
 * Archivo: FormaPago.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.enums
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Sebastian Jaimes: Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.enums;

import org.springframework.http.HttpStatus;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;

public enum FormaPago {
    CATEDRA,
    SALARIO;

    public static FormaPago from(String value) {
        if (value == null || value.isBlank()) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de vinculación no tiene forma de pago.");
        }
        String normalizado = value.trim().toUpperCase();
        try {
            return FormaPago.valueOf(normalizado);
        } catch (IllegalArgumentException ex) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La forma de pago no está configurada: " + value.trim());
        }
    }
}
