package br.com.diego.soares.dto;

import br.com.diego.soares.entity.Matricula;

import java.time.LocalDateTime;

public record MatriculaResposta(
        Long id,
        IdNomeResposta disciplina,
        IdNomeResposta professor,
        HorarioResposta horario,
        LocalDateTime dataMatricula) {

    public static MatriculaResposta de(Matricula matricula) {
        var matriz = matricula.getMatrizCurricular();
        return new MatriculaResposta(
                matricula.getId(),
                new IdNomeResposta(matriz.getDisciplina().id, matriz.getDisciplina().getNome()),
                new IdNomeResposta(matriz.getProfessor().id, matriz.getProfessor().getNome()),
                new HorarioResposta(
                        matriz.getHorario().id,
                        matriz.getHorario().getDiaSemana(),
                        matriz.getHorario().getHoraInicio(),
                        matriz.getHorario().getHoraFim()),
                matricula.getDataMatricula());
    }
}
