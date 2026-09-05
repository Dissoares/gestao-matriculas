package br.com.diego.soares.dto;

import br.com.diego.soares.entity.MatrizCurricular;

public record AulaDisponivelResposta(
        Long id,
        IdNomeResposta disciplina,
        IdNomeResposta professor,
        HorarioResposta horario,
        int vagasDisponiveis) {

    public static AulaDisponivelResposta de(MatrizCurricular matriz, long vagasOcupadas) {
        return new AulaDisponivelResposta(
                matriz.getId(),
                new IdNomeResposta(matriz.getDisciplina().id, matriz.getDisciplina().getNome()),
                new IdNomeResposta(matriz.getProfessor().id, matriz.getProfessor().getNome()),
                new HorarioResposta(
                        matriz.getHorario().id,
                        matriz.getHorario().getDiaSemana(),
                        matriz.getHorario().getHoraInicio(),
                        matriz.getHorario().getHoraFim()),
                (int) Math.max(0L, (long) matriz.getQuantidadeMaximaAlunos() - vagasOcupadas));
    }
}
