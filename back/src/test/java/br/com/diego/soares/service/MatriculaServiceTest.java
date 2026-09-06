package br.com.diego.soares.service;

import br.com.diego.soares.repository.MatrizCurricularRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import br.com.diego.soares.repository.MatriculaRepository;
import br.com.diego.soares.repository.AlunoRepository;
import br.com.diego.soares.exception.ExcecaoNegocio;
import br.com.diego.soares.entity.MatrizCurricular;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.diego.soares.mapper.MatriculaMapper;
import br.com.diego.soares.enums.DiaSemanaEnum;
import static org.mockito.ArgumentMatchers.any;
import br.com.diego.soares.mapper.MatrizMapper;
import static org.mockito.ArgumentMatchers.eq;
import br.com.diego.soares.entity.Disciplina;
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

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock AlunoRepository repositorioAluno;
    @Mock MatrizCurricularRepository repositorioMatriz;
    @Mock MatriculaRepository repositorioMatricula;
    @Mock MatrizMapper matrizMapper;
    @Mock MatriculaMapper matriculaMapper;

    private MatriculaService novoServico() {
        return new MatriculaService(
                repositorioAluno,
                repositorioMatriz,
                repositorioMatricula,
                matrizMapper,
                matriculaMapper
        );
    }

    private Aluno alunoDoCurso(Long idCurso) {
        Curso curso = new Curso();
        curso.id = idCurso;
        Aluno aluno = new Aluno();
        aluno.id = 100L;
        aluno.setCurso(curso);
        return aluno;
    }

    private MatrizCurricular matrizComCursoAutorizado(Long idCurso) {
        Curso curso = new Curso();
        curso.id = idCurso;

        Disciplina disciplina = new Disciplina();
        disciplina.id = 4L;
        disciplina.setNome("Algoritmos");

        Professor professor = new Professor();
        professor.id = 2L;
        professor.setNome("Ana");

        Horario horario = new Horario();
        horario.id = 8L;
        horario.setDiaSemana(DiaSemanaEnum.SEGUNDA);
        horario.setHoraInicio(LocalTime.of(8, 0));
        horario.setHoraFim(LocalTime.of(10, 0));

        MatrizCurricular matriz = new MatrizCurricular();
        matriz.setAtivo(true);
        matriz.setDisciplina(disciplina);
        matriz.setProfessor(professor);
        matriz.setHorario(horario);
        matriz.setCursosAutorizados(List.of(curso));
        matriz.setQuantidadeMaximaAlunos(30);
        return matriz;
    }

    @Test
    void devePersistirMatriculaQuandoTodasAsRegrasSaoAtendidas() {
        Aluno aluno = alunoDoCurso(1L);
        MatrizCurricular matriz = matrizComCursoAutorizado(1L);

        when(repositorioAluno.buscarPorIdKeycloak("aluno-1")).thenReturn(Optional.of(aluno));
        when(repositorioMatriz.buscarPorIdParaAtualizacao(10L)).thenReturn(matriz);
        when(repositorioMatricula.existePorAlunoEMatriz(aluno.getId(), 10L)).thenReturn(false);
        when(repositorioMatricula.contarPorIdMatriz(10L)).thenReturn(0L);
        when(repositorioMatricula.existeConflitoDeHorario(eq("aluno-1"), eq(DiaSemanaEnum.SEGUNDA), any(), any())).thenReturn(false);

        novoServico().matricular(10L, "aluno-1");

        ArgumentCaptor<br.com.diego.soares.entity.Matricula> captor = ArgumentCaptor.forClass(br.com.diego.soares.entity.Matricula.class);
        verify(repositorioMatricula).persist((br.com.diego.soares.entity.Matricula) captor.capture());
        assertEquals(aluno, captor.getValue().getAluno());
        assertEquals(matriz, captor.getValue().getMatrizCurricular());
    }

    @Test
    void deveImpedirMatriculaQuandoAlunoNaoExiste() {
        when(repositorioAluno.buscarPorIdKeycloak("aluno-1")).thenReturn(Optional.empty());

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () ->
                novoServico().matricular(10L, "aluno-1")
        );

        assertEquals(Response.Status.NOT_FOUND, excecao.getStatus());
        verify(repositorioMatricula, never()).persist(any(br.com.diego.soares.entity.Matricula.class));
    }

    @Test
    void deveImpedirMatriculaQuandoAulaNaoExiste() {
        when(repositorioAluno.buscarPorIdKeycloak("aluno-1")).thenReturn(Optional.of(alunoDoCurso(1L)));
        when(repositorioMatriz.buscarPorIdParaAtualizacao(10L)).thenReturn(null);

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () ->
                novoServico().matricular(10L, "aluno-1")
        );

        assertEquals(Response.Status.NOT_FOUND, excecao.getStatus());
        verify(repositorioMatricula, never()).persist(any(br.com.diego.soares.entity.Matricula.class));
    }

    @Test
    void deveImpedirMatriculaQuandoCursoNaoEstaAutorizado() {
        Aluno aluno = alunoDoCurso(1L);
        MatrizCurricular matriz = matrizComCursoAutorizado(2L);

        when(repositorioAluno.buscarPorIdKeycloak("aluno-1")).thenReturn(Optional.of(aluno));
        when(repositorioMatriz.buscarPorIdParaAtualizacao(10L)).thenReturn(matriz);

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () ->
                novoServico().matricular(10L, "aluno-1")
        );

        assertEquals("curso_nao_autorizado", excecao.getCodigo());
        verify(repositorioMatricula, never()).persist(any(br.com.diego.soares.entity.Matricula.class));
    }

    @Test
    void deveImpedirMatriculaQuandoAlunoJaEstaMatriculado() {
        Aluno aluno = alunoDoCurso(1L);
        MatrizCurricular matriz = matrizComCursoAutorizado(1L);

        when(repositorioAluno.buscarPorIdKeycloak("aluno-1")).thenReturn(Optional.of(aluno));
        when(repositorioMatriz.buscarPorIdParaAtualizacao(10L)).thenReturn(matriz);
        when(repositorioMatricula.existePorAlunoEMatriz(aluno.getId(), 10L)).thenReturn(true);

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () ->
                novoServico().matricular(10L, "aluno-1")
        );

        assertEquals("regra_de_negocio", excecao.getCodigo());
        verify(repositorioMatricula, never()).persist(any(br.com.diego.soares.entity.Matricula.class));
    }

    @Test
    void deveImpedirMatriculaQuandoNaoHaVaga() {
        Aluno aluno = alunoDoCurso(1L);
        MatrizCurricular matriz = matrizComCursoAutorizado(1L);
        matriz.setQuantidadeMaximaAlunos(1);

        when(repositorioAluno.buscarPorIdKeycloak("aluno-1")).thenReturn(Optional.of(aluno));
        when(repositorioMatriz.buscarPorIdParaAtualizacao(10L)).thenReturn(matriz);
        when(repositorioMatricula.existePorAlunoEMatriz(aluno.getId(), 10L)).thenReturn(false);
        when(repositorioMatricula.contarPorIdMatriz(10L)).thenReturn(1L);

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () ->
                novoServico().matricular(10L, "aluno-1")
        );

        assertEquals("regra_de_negocio", excecao.getCodigo());
        verify(repositorioMatricula, never()).persist(any(br.com.diego.soares.entity.Matricula.class));
    }

    @Test
    void deveImpedirMatriculaQuandoHaConflitoDeHorario() {
        Aluno aluno = alunoDoCurso(1L);
        MatrizCurricular matriz = matrizComCursoAutorizado(1L);

        when(repositorioAluno.buscarPorIdKeycloak("aluno-1")).thenReturn(Optional.of(aluno));
        when(repositorioMatriz.buscarPorIdParaAtualizacao(10L)).thenReturn(matriz);
        when(repositorioMatricula.existePorAlunoEMatriz(aluno.getId(), 10L)).thenReturn(false);
        when(repositorioMatricula.contarPorIdMatriz(10L)).thenReturn(0L);
        when(repositorioMatricula.existeConflitoDeHorario(eq("aluno-1"), eq(DiaSemanaEnum.SEGUNDA), any(), any())).thenReturn(true);

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () ->
                novoServico().matricular(10L, "aluno-1")
        );

        assertEquals("regra_de_negocio", excecao.getCodigo());
        verify(repositorioMatricula, never()).persist(any(br.com.diego.soares.entity.Matricula.class));
    }
}
