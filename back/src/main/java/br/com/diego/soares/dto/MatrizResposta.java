package br.com.diego.soares.dto;

import java.util.List;

public record MatrizResposta(
        Long id,
        IdNomeResposta disciplina,
        IdNomeResposta professor,
        HorarioResposta horario,
        List<IdNomeResposta> cursosAutorizados,
        Integer quantidadeMaximaAlunos,
        long vagasOcupadas,
        boolean ativo
) {
}
