package co.edu.unipamplona.ciadti.rvd.util;

public class NormalizarParametros {
    public static String normalizeParam(String param) {
        if (param == null || param.isBlank()) {
            return null;
        }
        return param.trim().toLowerCase();
    }

}
