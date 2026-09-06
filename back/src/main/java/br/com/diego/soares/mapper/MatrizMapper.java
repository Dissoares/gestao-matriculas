package br.com.diego.soares.mapper;

import br.com.diego.soares.dto.AulaDisponivelResposta;
import br.com.diego.soares.dto.HorarioResposta;
import br.com.diego.soares.dto.IdNomeResposta;
import br.com.diego.soares.dto.MatrizResposta;
import br.com.diego.soares.entity.Curso;
import br.com.diego.soares.entity.Horario;
import br.com.diego.soares.entity.MatrizCurricular;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MatrizMapper {

    public MatrizResposta paraResposta(MatrizCurricular matriz, long vagasOcupadas) {
        return new MatrizResposta(
                matriz.getId(),
                new IdNomeResposta(matriz.getDisciplina().getId(), matriz.getDisciplina().getNome()),
                new IdNomeResposta(matriz.getProfessor().getId(), matriz.getProfessor().getNome()),
                paraHorarioResposta(matriz.getHorario()),
                matriz.getCursosAutorizados().stream().map(this::paraCursoResposta).toList(),
                matriz.getQuantidadeMaximaAlunos(),
                vagasOcupadas,
                matriz.isAtivo());
    }

    public AulaDisponivelResposta paraAulaDisponivel(MatrizCurricular matriz, long vagasOcupadas) {
        return new AulaDisponivelResposta(
                matriz.getId(),
                new IdNomeResposta(matriz.getDisciplina().getId(), matriz.getDisciplina().getNome()),
                new IdNomeResposta(matriz.getProfessor().getId(), matriz.getProfessor().getNome()),
                paraHorarioResposta(matriz.getHorario()),
                (int) Math.max(0L, (long) matriz.getQuantidadeMaximaAlunos() - vagasOcupadas));
    }

    private HorarioResposta paraHorarioResposta(Horario horario) {
        return new HorarioResposta(horario.getId(), horario.getDiaSemana(), horario.getHoraInicio(), horario.getHoraFim());
    }

    private IdNomeResposta paraCursoResposta(Curso curso) {
        return new IdNomeResposta(curso.getId(), curso.getNome());
    }
}
