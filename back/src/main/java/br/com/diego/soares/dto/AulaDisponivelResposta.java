package br.com.diego.soares.dto;

public record AulaDisponivelResposta(
        Long id,
        IdNomeResposta disciplina,
        IdNomeResposta professor,
        HorarioResposta horario,
        int vagasDisponiveis) {
}
