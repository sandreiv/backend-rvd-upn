package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadDetalleDTO(
        Long id,
        Long idPadre,
        Long idDependencia,
        String orden,
        String nombre,
        String descripcion,
        String componente,
        String minimoHoras,
        String maximoHoras,
        String docenciaDirecta,
        String horasPorSemestre,
        String aplicaProyectos,
        String estado
) {}
