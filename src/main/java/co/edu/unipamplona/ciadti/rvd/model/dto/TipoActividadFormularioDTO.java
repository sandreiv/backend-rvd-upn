package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadFormularioDTO(
        String nombre,
        String descripcion,
        String componente,
        String orden,
        Long idDependencia,
        String docenciaDirecta,
        String horasPorSemestre,
        String aplicaProyectos,
        Integer minimoHoras,
        Integer maximoHoras,
        String estado
) {}
