package br.com.diego.soares.dto;

import br.com.diego.soares.entity.Curso;
import br.com.diego.soares.entity.MatrizCurricular;

import java.util.List;

public record MatrizResposta(
        Long id,
        IdNomeResposta disciplina,
        IdNomeResposta professor,
        HorarioResposta horario,
        List<IdNomeResposta> cursosAutorizados,
        Integer quantidadeMaximaAlunos,
        long vagasOcupadas,
        boolean ativo) {

    public static MatrizResposta de(MatrizCurricular matriz, long vagasOcupadas) {
        return new MatrizResposta(
                matriz.getId(),
                new IdNomeResposta(matriz.getDisciplina().id, matriz.getDisciplina().getNome()),
                new IdNomeResposta(matriz.getProfessor().id, matriz.getProfessor().getNome()),
                new HorarioResposta(
                        matriz.getHorario().id,
                        matriz.getHorario().getDiaSemana(),
                        matriz.getHorario().getHoraInicio(),
                        matriz.getHorario().getHoraFim()),
                matriz.getCursosAutorizados().stream().map(MatrizResposta::curso).toList(),
                matriz.getQuantidadeMaximaAlunos(),
                vagasOcupadas,
                Boolean.TRUE.equals(matriz.getAtivo()));
    }

    private static IdNomeResposta curso(Curso curso) {
        return new IdNomeResposta(curso.id, curso.getNome());
    }
}
