package br.com.diego.soares.service;

import br.com.diego.soares.dto.RequisicaoCriarMatriz;
import br.com.diego.soares.entity.Coordenador;
import br.com.diego.soares.entity.Curso;
import br.com.diego.soares.entity.Disciplina;
import br.com.diego.soares.entity.Horario;
import br.com.diego.soares.entity.Professor;
import br.com.diego.soares.exception.ExcecaoNegocio;
import br.com.diego.soares.repository.CoordenadorRepository;
import br.com.diego.soares.repository.CursoRepository;
import br.com.diego.soares.repository.DisciplinaRepository;
import br.com.diego.soares.repository.HorarioRepository;
import br.com.diego.soares.repository.MatriculaRepository;
import br.com.diego.soares.repository.MatrizCurricularRepository;
import br.com.diego.soares.repository.ProfessorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatrizCurricularServiceTest {

    @Mock MatrizCurricularRepository repositorioMatriz;
    @Mock CoordenadorRepository repositorioCoordenador;
    @Mock DisciplinaRepository repositorioDisciplina;
    @Mock ProfessorRepository repositorioProfessor;
    @Mock HorarioRepository repositorioHorario;
    @Mock CursoRepository repositorioCurso;
    @Mock MatriculaRepository repositorioMatricula;

    @Test
    void deveImpedirOfertaDaMesmaDisciplinaNoMesmoHorario() {
        RequisicaoCriarMatriz requisicao = new RequisicaoCriarMatriz(1L, 2L, 3L, Set.of(4L), 30);

        when(repositorioCoordenador.buscarPorIdKeycloak("coordenador-1")).thenReturn(Optional.of(new Coordenador()));

        Disciplina disciplina = new Disciplina();
        disciplina.id = 1L;
        when(repositorioDisciplina.findByIdOptional(1L)).thenReturn(Optional.of(disciplina));

        when(repositorioProfessor.findByIdOptional(2L)).thenReturn(Optional.of(new Professor()));

        Horario horario = new Horario();
        horario.id = 3L;
        horario.setHoraInicio(java.time.LocalTime.of(8, 0));
        horario.setHoraFim(java.time.LocalTime.of(10, 0));
        when(repositorioHorario.findByIdOptional(3L)).thenReturn(Optional.of(horario));

        when(repositorioCurso.findByIdOptional(4L)).thenReturn(Optional.of(new Curso()));
        when(repositorioMatriz.existeOfertaAtivaDaDisciplinaNoHorario(1L, 3L, null)).thenReturn(true);

        assertThrows(ExcecaoNegocio.class, () -> novoServico().criar(requisicao, "coordenador-1"));
        org.mockito.Mockito.verify(repositorioMatriz, never()).persist(any());
    }

    private MatrizCurricularService novoServico() {
        return new MatrizCurricularService(
                repositorioMatriz,
                repositorioCoordenador,
                repositorioDisciplina,
                repositorioProfessor,
                repositorioHorario,
                repositorioCurso,
                repositorioMatricula);
    }
}
