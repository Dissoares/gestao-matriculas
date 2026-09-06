package br.com.diego.soares.service;

import br.com.diego.soares.repository.MatrizCurricularRepository;
import br.com.diego.soares.repository.CoordenadorRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import br.com.diego.soares.repository.DisciplinaRepository;
import static org.junit.jupiter.api.Assertions.assertFalse;
import br.com.diego.soares.repository.MatriculaRepository;
import br.com.diego.soares.repository.ProfessorRepository;
import static org.junit.jupiter.api.Assertions.assertTrue;
import br.com.diego.soares.dto.RequisicaoAtualizarMatriz;
import br.com.diego.soares.repository.HorarioRepository;
import br.com.diego.soares.repository.CursoRepository;
import br.com.diego.soares.dto.RequisicaoCriarMatriz;
import br.com.diego.soares.exception.ExcecaoNegocio;
import br.com.diego.soares.entity.MatrizCurricular;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import br.com.diego.soares.mapper.MatrizMapper;
import br.com.diego.soares.entity.Coordenador;
import br.com.diego.soares.entity.Disciplina;
import br.com.diego.soares.entity.Matricula;
import br.com.diego.soares.entity.Professor;
import br.com.diego.soares.entity.Horario;
import static org.mockito.Mockito.verify;
import br.com.diego.soares.entity.Aluno;
import br.com.diego.soares.entity.Curso;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.time.LocalTime;
import java.util.Optional;
import org.mockito.Mock;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class MatrizCurricularServiceTest {

    @Mock MatrizCurricularRepository repositorioMatriz;
    @Mock CoordenadorRepository repositorioCoordenador;
    @Mock DisciplinaRepository repositorioDisciplina;
    @Mock ProfessorRepository repositorioProfessor;
    @Mock HorarioRepository repositorioHorario;
    @Mock CursoRepository repositorioCurso;
    @Mock MatriculaRepository repositorioMatricula;
    @Mock MatrizMapper matrizMapper;

    private MatrizCurricularService novoServico() {
        return new MatrizCurricularService(
                repositorioMatriz,
                repositorioCoordenador,
                repositorioDisciplina,
                repositorioProfessor,
                repositorioHorario,
                repositorioCurso,
                repositorioMatricula,
                matrizMapper
        );
    }

    private Disciplina disciplinaComId(Long id) {
        Disciplina disciplina = new Disciplina();
        disciplina.id = id;
        return disciplina;
    }

    private Professor professorComId(Long id) {
        Professor professor = new Professor();
        professor.id = id;
        return professor;
    }

    private Horario horarioValido(Long id) {
        Horario horario = new Horario();
        horario.id = id;
        horario.setHoraInicio(LocalTime.of(8, 0));
        horario.setHoraFim(LocalTime.of(10, 0));
        return horario;
    }

    private Curso cursoComId(Long id) {
        Curso curso = new Curso();
        curso.id = id;
        return curso;
    }

    @Test
    void deveCriarMatrizComDadosValidos() {
        Disciplina disciplina = disciplinaComId(1L);
        Professor professor = professorComId(2L);
        Horario horario = horarioValido(3L);
        Curso curso = cursoComId(4L);

        when(repositorioCoordenador.buscarPorIdKeycloak("coordenador-1")).thenReturn(Optional.of(new Coordenador()));
        when(repositorioDisciplina.findByIdOptional(1L)).thenReturn(Optional.of(disciplina));
        when(repositorioProfessor.findByIdOptional(2L)).thenReturn(Optional.of(professor));
        when(repositorioHorario.findByIdOptional(3L)).thenReturn(Optional.of(horario));
        when(repositorioCurso.findByIdOptional(4L)).thenReturn(Optional.of(curso));
        when(repositorioMatriz.existeOfertaAtivaDaDisciplinaNoHorario(1L, 3L, null)).thenReturn(false);

        novoServico().criar(new RequisicaoCriarMatriz(1L, 2L, 3L, Set.of(4L), 30), "coordenador-1");

        ArgumentCaptor<MatrizCurricular> captor = ArgumentCaptor.forClass(MatrizCurricular.class);
        verify(repositorioMatriz).persist((MatrizCurricular) captor.capture());
        MatrizCurricular criada = captor.getValue();
        assertEquals(disciplina, criada.getDisciplina());
        assertEquals(horario, criada.getHorario());
        assertTrue(criada.isAtivo());
    }

    @Test
    void deveImpedirCriacaoQuandoCoordenadorNaoExiste() {
        when(repositorioCoordenador.buscarPorIdKeycloak("coordenador-1")).thenReturn(Optional.empty());

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () -> novoServico().criar(new RequisicaoCriarMatriz(1L, 2L, 3L, Set.of(4L), 30), "coordenador-1"));

        assertEquals(Response.Status.NOT_FOUND, excecao.getStatus());
        verify(repositorioMatriz, never()).persist(any(MatrizCurricular.class));
    }

    @Test
    void deveImpedirCriacaoQuandoDisciplinaNaoExiste() {
        when(repositorioCoordenador.buscarPorIdKeycloak("coordenador-1")).thenReturn(Optional.of(new Coordenador()));
        when(repositorioDisciplina.findByIdOptional(1L)).thenReturn(Optional.empty());

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () -> novoServico().criar(new RequisicaoCriarMatriz(1L, 2L, 3L, Set.of(4L), 30), "coordenador-1"));

        assertEquals(Response.Status.NOT_FOUND, excecao.getStatus());
        verify(repositorioMatriz, never()).persist(any(MatrizCurricular.class));
    }

    @Test
    void deveImpedirCriacaoQuandoProfessorNaoExiste() {
        when(repositorioCoordenador.buscarPorIdKeycloak("coordenador-1")).thenReturn(Optional.of(new Coordenador()));
        when(repositorioDisciplina.findByIdOptional(1L)).thenReturn(Optional.of(disciplinaComId(1L)));
        when(repositorioProfessor.findByIdOptional(2L)).thenReturn(Optional.empty());

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () -> novoServico().criar(new RequisicaoCriarMatriz(1L, 2L, 3L, Set.of(4L), 30), "coordenador-1"));

        assertEquals(Response.Status.NOT_FOUND, excecao.getStatus());
        verify(repositorioMatriz, never()).persist(any(MatrizCurricular.class));
    }

    @Test
    void deveImpedirCriacaoQuandoHorarioNaoExiste() {
        when(repositorioCoordenador.buscarPorIdKeycloak("coordenador-1")).thenReturn(Optional.of(new Coordenador()));
        when(repositorioDisciplina.findByIdOptional(1L)).thenReturn(Optional.of(disciplinaComId(1L)));
        when(repositorioProfessor.findByIdOptional(2L)).thenReturn(Optional.of(professorComId(2L)));
        when(repositorioHorario.findByIdOptional(3L)).thenReturn(Optional.empty());

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () -> novoServico().criar(new RequisicaoCriarMatriz(1L, 2L, 3L, Set.of(4L), 30), "coordenador-1"));

        assertEquals(Response.Status.NOT_FOUND, excecao.getStatus());
        verify(repositorioMatriz, never()).persist(any(MatrizCurricular.class));
    }

    @Test
    void deveImpedirCriacaoQuandoCursoNaoExiste() {
        when(repositorioCoordenador.buscarPorIdKeycloak("coordenador-1")).thenReturn(Optional.of(new Coordenador()));
        when(repositorioDisciplina.findByIdOptional(1L)).thenReturn(Optional.of(disciplinaComId(1L)));
        when(repositorioProfessor.findByIdOptional(2L)).thenReturn(Optional.of(professorComId(2L)));
        when(repositorioHorario.findByIdOptional(3L)).thenReturn(Optional.of(horarioValido(3L)));
        when(repositorioCurso.findByIdOptional(4L)).thenReturn(Optional.empty());

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () -> novoServico().criar(new RequisicaoCriarMatriz(1L, 2L, 3L, Set.of(4L), 30), "coordenador-1"));

        assertEquals(Response.Status.NOT_FOUND, excecao.getStatus());
        verify(repositorioMatriz, never()).persist(any(MatrizCurricular.class));
    }

    @Test
    void deveImpedirOfertaDaMesmaDisciplinaNoMesmoHorario() {
        when(repositorioCoordenador.buscarPorIdKeycloak("coordenador-1")).thenReturn(Optional.of(new Coordenador()));
        when(repositorioDisciplina.findByIdOptional(1L)).thenReturn(Optional.of(disciplinaComId(1L)));
        when(repositorioProfessor.findByIdOptional(2L)).thenReturn(Optional.of(professorComId(2L)));
        when(repositorioHorario.findByIdOptional(3L)).thenReturn(Optional.of(horarioValido(3L)));
        when(repositorioCurso.findByIdOptional(4L)).thenReturn(Optional.of(cursoComId(4L)));
        when(repositorioMatriz.existeOfertaAtivaDaDisciplinaNoHorario(1L, 3L, null)).thenReturn(true);

        assertThrows(ExcecaoNegocio.class, () -> novoServico().criar(new RequisicaoCriarMatriz(1L, 2L, 3L, Set.of(4L), 30), "coordenador-1"));

        verify(repositorioMatriz, never()).persist(any(MatrizCurricular.class));
    }

    @Test
    void deveImpedirAtualizacaoQuandoMatrizNaoExiste() {
        when(repositorioMatriz.buscarAtivaDoCoordenadorPorId(10L, "coordenador-1")).thenReturn(Optional.empty());

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () -> novoServico().atualizar(new RequisicaoAtualizarMatriz(2L, 3L, Set.of(1L)), 10L, "coordenador-1"));

        assertEquals(Response.Status.NOT_FOUND, excecao.getStatus());
    }

    @Test
    void deveImpedirAtualizacaoQuandoRemoveriaAlunoMatriculado() {
        Disciplina disciplina = disciplinaComId(1L);
        MatrizCurricular matrizExistente = new MatrizCurricular();
        matrizExistente.setDisciplina(disciplina);
        matrizExistente.setAtivo(true);

        Curso cursoDoAluno = cursoComId(5L);
        Aluno aluno = new Aluno();
        aluno.setCurso(cursoDoAluno);
        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);

        when(repositorioMatriz.buscarAtivaDoCoordenadorPorId(10L, "coordenador-1")).thenReturn(Optional.of(matrizExistente));
        when(repositorioProfessor.findByIdOptional(2L)).thenReturn(Optional.of(professorComId(2L)));
        when(repositorioHorario.findByIdOptional(3L)).thenReturn(Optional.of(horarioValido(3L)));
        when(repositorioCurso.findByIdOptional(1L)).thenReturn(Optional.of(cursoComId(1L)));
        when(repositorioMatriz.existeOfertaAtivaDaDisciplinaNoHorario(1L, 3L, 10L)).thenReturn(false);
        when(repositorioMatricula.buscarPorIdMatriz(10L)).thenReturn(List.of(matricula));

        assertThrows(ExcecaoNegocio.class, () -> novoServico().atualizar(new RequisicaoAtualizarMatriz(2L, 3L, Set.of(1L)), 10L, "coordenador-1"));
    }

    @Test
    void deveExcluirLogicamenteQuandoNaoHaMatriculas() {
        MatrizCurricular matriz = new MatrizCurricular();
        matriz.setAtivo(true);

        when(repositorioMatriz.buscarAtivaDoCoordenadorPorId(10L, "coordenador-1")).thenReturn(Optional.of(matriz));
        when(repositorioMatricula.contarPorIdMatriz(10L)).thenReturn(0L);

        novoServico().excluir(10L, "coordenador-1");

        assertFalse(matriz.isAtivo());
    }

    @Test
    void deveImpedirExclusaoLogicaQuandoExistemMatriculasAtivas() {
        MatrizCurricular matriz = new MatrizCurricular();
        matriz.setAtivo(true);

        when(repositorioMatriz.buscarAtivaDoCoordenadorPorId(10L, "coordenador-1")).thenReturn(Optional.of(matriz));
        when(repositorioMatricula.contarPorIdMatriz(10L)).thenReturn(2L);

        assertThrows(ExcecaoNegocio.class, () -> novoServico().excluir(10L, "coordenador-1"));
        assertTrue(matriz.isAtivo());
    }
}
