package br.com.diego.soares.service;

import br.com.diego.soares.dto.RequisicaoAtualizarMatriz;
import br.com.diego.soares.dto.RequisicaoCriarMatriz;
import br.com.diego.soares.entity.Aluno;
import br.com.diego.soares.entity.Coordenador;
import br.com.diego.soares.entity.Curso;
import br.com.diego.soares.entity.Disciplina;
import br.com.diego.soares.entity.Horario;
import br.com.diego.soares.entity.Matricula;
import br.com.diego.soares.entity.MatrizCurricular;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
        horario.setHoraInicio(LocalTime.of(8, 0));
        horario.setHoraFim(LocalTime.of(10, 0));
        when(repositorioHorario.findByIdOptional(3L)).thenReturn(Optional.of(horario));

        when(repositorioCurso.findByIdOptional(4L)).thenReturn(Optional.of(new Curso()));
        when(repositorioMatriz.existeOfertaAtivaDaDisciplinaNoHorario(1L, 3L, null)).thenReturn(true);

        assertThrows(ExcecaoNegocio.class, () -> novoServico().criar(requisicao, "coordenador-1"));
        verify(repositorioMatriz, never()).persist(any());
    }

    @Test
    void deveImpedirAtualizacaoQuandoRemoveriaAlunoMatriculado() {
        Disciplina disciplina = new Disciplina();
        disciplina.id = 1L;
        MatrizCurricular matrizExistente = new MatrizCurricular();
        matrizExistente.setDisciplina(disciplina);
        matrizExistente.setAtivo(true);

        Professor professor = new Professor();
        professor.id = 2L;

        Horario horario = new Horario();
        horario.id = 3L;
        horario.setHoraInicio(LocalTime.of(8, 0));
        horario.setHoraFim(LocalTime.of(10, 0));

        Curso cursoNovo = new Curso();
        cursoNovo.id = 1L;

        Curso cursoDoAluno = new Curso();
        cursoDoAluno.id = 5L;
        Aluno aluno = new Aluno();
        aluno.setCurso(cursoDoAluno);
        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);

        RequisicaoAtualizarMatriz requisicao = new RequisicaoAtualizarMatriz(2L, 3L, Set.of(1L));

        when(repositorioMatriz.buscarAtivaDoCoordenadorPorId(10L, "coordenador-1"))
                .thenReturn(Optional.of(matrizExistente));
        when(repositorioProfessor.findByIdOptional(2L)).thenReturn(Optional.of(professor));
        when(repositorioHorario.findByIdOptional(3L)).thenReturn(Optional.of(horario));
        when(repositorioCurso.findByIdOptional(1L)).thenReturn(Optional.of(cursoNovo));
        when(repositorioMatriz.existeOfertaAtivaDaDisciplinaNoHorario(1L, 3L, 10L)).thenReturn(false);
        when(repositorioMatricula.buscarPorIdMatriz(10L)).thenReturn(List.of(matricula));

        assertThrows(ExcecaoNegocio.class, () -> novoServico().atualizar(requisicao, 10L, "coordenador-1"));
    }

    @Test
    void deveImpedirExclusaoLogicaQuandoExistemMatriculasAtivas() {
        MatrizCurricular matrizExistente = new MatrizCurricular();
        matrizExistente.setAtivo(true);

        when(repositorioMatriz.buscarAtivaDoCoordenadorPorId(10L, "coordenador-1"))
                .thenReturn(Optional.of(matrizExistente));
        when(repositorioMatricula.contarPorIdMatriz(10L)).thenReturn(3L);

        assertThrows(ExcecaoNegocio.class, () -> novoServico().excluir(10L, "coordenador-1"));
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
