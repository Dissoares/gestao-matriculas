package br.com.diego.soares.dto;

import java.time.LocalDateTime;

public record MatriculaResposta(
        Long id,
        IdNomeResposta disciplina,
        IdNomeResposta professor,
        HorarioResposta horario,
        LocalDateTime dataMatricula) {
}
