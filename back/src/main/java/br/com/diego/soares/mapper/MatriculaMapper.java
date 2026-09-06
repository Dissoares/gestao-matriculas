package br.com.diego.soares.mapper;

import br.com.diego.soares.dto.HorarioResposta;
import br.com.diego.soares.dto.IdNomeResposta;
import br.com.diego.soares.dto.MatriculaResposta;
import br.com.diego.soares.entity.Horario;
import br.com.diego.soares.entity.Matricula;
import br.com.diego.soares.entity.MatrizCurricular;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MatriculaMapper {

    public MatriculaResposta paraResposta(Matricula matricula) {
        MatrizCurricular matriz = matricula.getMatrizCurricular();
        Horario horario = matriz.getHorario();
        return new MatriculaResposta(
                matricula.getId(),
                new IdNomeResposta(matriz.getDisciplina().id, matriz.getDisciplina().getNome()),
                new IdNomeResposta(matriz.getProfessor().id, matriz.getProfessor().getNome()),
                new HorarioResposta(horario.id, horario.getDiaSemana(), horario.getHoraInicio(), horario.getHoraFim()),
                matricula.getDataMatricula());
    }
}
