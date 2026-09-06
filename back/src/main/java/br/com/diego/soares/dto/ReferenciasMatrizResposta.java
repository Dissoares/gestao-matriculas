package br.com.diego.soares.dto;

import java.util.List;

public record ReferenciasMatrizResposta(
        List<IdNomeResposta> disciplinas,
        List<IdNomeResposta> professores,
        List<HorarioResposta> horarios,
        List<IdNomeResposta> cursos
) {
}
