package co.edu.unipamplona.ciadti.rvd.model.dto;

public record MateriaParticularDTO(
    Long id,
    MateriaDTO materia,
    ProgramaDTO programa,
    String tipoMateria,
    Long docentesXgrupo
) {}
