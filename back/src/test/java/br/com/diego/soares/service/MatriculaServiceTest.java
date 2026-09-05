package br.com.diego.soares.service;

import br.com.diego.soares.entity.Aluno;
import br.com.diego.soares.entity.Curso;
import br.com.diego.soares.entity.Disciplina;
import br.com.diego.soares.entity.Horario;
import br.com.diego.soares.entity.MatrizCurricular;
import br.com.diego.soares.entity.Professor;
import br.com.diego.soares.enums.DiaSemanaEnum;
import br.com.diego.soares.exception.ExcecaoNegocio;
import br.com.diego.soares.repository.AlunoRepository;
import br.com.diego.soares.repository.MatriculaRepository;
import br.com.diego.soares.repository.MatrizCurricularRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock AlunoRepository repositorioAluno;
    @Mock MatrizCurricularRepository repositorioMatriz;
    @Mock MatriculaRepository repositorioMatricula;

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
    void deveImpedirMatriculaQuandoCursoNaoEstaAutorizado() {
        MatriculaService servico = novoServico();
        Aluno aluno = alunoDoCurso(1L);
        MatrizCurricular matriz = matrizComCursoAutorizado(2L);
        when(repositorioAluno.buscarPorIdKeycloak("aluno-1")).thenReturn(Optional.of(aluno));
        when(repositorioMatriz.buscarPorIdParaAtualizacao(10L)).thenReturn(matriz);

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () -> servico.matricular(10L, "aluno-1"));

        assertEquals("curso_nao_autorizado", excecao.obterCodigo());
        verify(repositorioMatricula, never()).persist(any());
    }

    @Test
    void deveImpedirMatriculaQuandoNaoHaVaga() {
        MatriculaService servico = novoServico();
        Aluno aluno = alunoDoCurso(1L);
        MatrizCurricular matriz = matrizComCursoAutorizado(1L);
        matriz.setQuantidadeMaximaAlunos(1);
        when(repositorioAluno.buscarPorIdKeycloak("aluno-1")).thenReturn(Optional.of(aluno));
        when(repositorioMatriz.buscarPorIdParaAtualizacao(10L)).thenReturn(matriz);
        when(repositorioMatricula.existePorAlunoEMatriz(aluno.id, 10L)).thenReturn(false);
        when(repositorioMatricula.contarPorIdMatriz(10L)).thenReturn(1L);

        ExcecaoNegocio excecao = assertThrows(ExcecaoNegocio.class, () -> servico.matricular(10L, "aluno-1"));

        assertEquals("regra_de_negocio", excecao.obterCodigo());
        verify(repositorioMatricula, never()).persist(any());
    }

    @Test
    void devePersistirMatriculaQuandoTodasAsRegrasSaoAtendidas() {
        MatriculaService servico = novoServico();
        Aluno aluno = alunoDoCurso(1L);
        MatrizCurricular matriz = matrizComCursoAutorizado(1L);
        when(repositorioAluno.buscarPorIdKeycloak("aluno-1")).thenReturn(Optional.of(aluno));
        when(repositorioMatriz.buscarPorIdParaAtualizacao(10L)).thenReturn(matriz);
        when(repositorioMatricula.existePorAlunoEMatriz(aluno.id, 10L)).thenReturn(false);
        when(repositorioMatricula.contarPorIdMatriz(10L)).thenReturn(0L);
        when(repositorioMatricula.existeConflitoDeHorario(eq("aluno-1"), eq(DiaSemanaEnum.SEGUNDA), any(), any())).thenReturn(false);

        servico.matricular(10L, "aluno-1");

        ArgumentCaptor<br.com.diego.soares.entity.Matricula> captor = ArgumentCaptor.forClass(br.com.diego.soares.entity.Matricula.class);
        verify(repositorioMatricula).persist(captor.capture());
        assertEquals(aluno, captor.getValue().getAluno());
        assertEquals(matriz, captor.getValue().getMatrizCurricular());
    }

    private MatriculaService novoServico() {
        return new MatriculaService(repositorioAluno, repositorioMatriz, repositorioMatricula);
    }


}
