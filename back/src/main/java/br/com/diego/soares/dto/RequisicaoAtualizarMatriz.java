package br.com.diego.soares.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record RequisicaoAtualizarMatriz(
        @NotNull @Positive Long professorId,
        @NotNull @Positive Long horarioId,
        @NotEmpty Set<@NotNull @Positive Long> cursosAutorizadosIds) {
}
