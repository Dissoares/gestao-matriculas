package br.com.diego.soares.service;

import br.com.diego.soares.repository.MatrizCurricularRepository;
import br.com.diego.soares.repository.MatriculaRepository;
import br.com.diego.soares.dto.AulaDisponivelResposta;
import br.com.diego.soares.repository.AlunoRepository;
import br.com.diego.soares.exception.ExcecaoNegocio;
import jakarta.enterprise.context.ApplicationScoped;
import br.com.diego.soares.entity.MatrizCurricular;
import br.com.diego.soares.dto.MatriculaResposta;
import br.com.diego.soares.entity.Matricula;
import jakarta.transaction.Transactional;
import br.com.diego.soares.entity.Aluno;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MatriculaService {

    private final AlunoRepository repositorioAluno;
    private final MatrizCurricularRepository repositorioMatriz;
    private final MatriculaRepository repositorioMatricula;

    @Inject
    public MatriculaService(AlunoRepository repositorioAluno, MatrizCurricularRepository repositorioMatriz, MatriculaRepository repositorioMatricula) {
        this.repositorioAluno = repositorioAluno;
        this.repositorioMatriz = repositorioMatriz;
        this.repositorioMatricula = repositorioMatricula;
    }

    @Transactional
    public List<AulaDisponivelResposta> listarAulasDisponiveis(String idKeycloak) {
        Aluno aluno = obterAluno(idKeycloak);
        return repositorioMatriz.buscarAulasDisponiveisParaCurso(aluno.getCurso().id).stream()
                .filter(matriz -> !repositorioMatricula.existePorAlunoEMatriz(aluno.id, matriz.getId()))
                .map(matriz -> AulaDisponivelResposta.de(
                        matriz, repositorioMatricula.contarPorIdMatriz(matriz.getId())))
                .filter(aula -> aula.vagasDisponiveis() > 0)
                .toList();
    }

    @Transactional
    public MatriculaResposta matricular(Long idMatriz, String idKeycloak) {
        Aluno aluno = obterAluno(idKeycloak);
        MatrizCurricular matriz = repositorioMatriz.buscarPorIdParaAtualizacao(idMatriz);

        if (matriz == null || !Boolean.TRUE.equals(matriz.getAtivo())) {
            throw naoEncontrado("aula disponível");
        }

        validarCursoAutorizado(aluno, matriz);

        if (repositorioMatricula.existePorAlunoEMatriz(aluno.id, idMatriz)) {
            throw new ExcecaoNegocio("Você já está matriculado nesta aula.");
        }

        if (repositorioMatricula.contarPorIdMatriz(idMatriz) >= matriz.getQuantidadeMaximaAlunos()) {
            throw new ExcecaoNegocio("Não há vagas disponíveis para esta aula.");
        }

        if (repositorioMatricula.existeConflitoDeHorario(idKeycloak, matriz.getHorario().getDiaSemana(), matriz.getHorario().getHoraInicio(), matriz.getHorario().getHoraFim())) {
            throw new ExcecaoNegocio("Esta aula possui conflito de horário com uma matrícula existente.");
        }

        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setMatrizCurricular(matriz);
        matricula.setDataMatricula(LocalDateTime.now());

        repositorioMatricula.persist(matricula);

        return MatriculaResposta.de(matricula);
    }

    @Transactional
    public List<MatriculaResposta> listarMinhasMatriculas(String idKeycloak) {
        obterAluno(idKeycloak);
        return repositorioMatricula.buscarDoAluno(idKeycloak).stream().map(MatriculaResposta::de).toList();
    }

    private Aluno obterAluno(String idKeycloak) {
        return repositorioAluno.buscarPorIdKeycloak(idKeycloak).orElseThrow(() -> naoEncontrado("aluno"));
    }

    private void validarCursoAutorizado(Aluno aluno, MatrizCurricular matriz) {
        boolean autorizado = matriz.getCursosAutorizados().stream().anyMatch(curso -> curso.id.equals(aluno.getCurso().id));

        if (!autorizado) {
            throw new ExcecaoNegocio(Response.Status.FORBIDDEN, "curso_nao_autorizado", "Esta aula não é autorizada para o seu curso.");
        }
    }

    private ExcecaoNegocio naoEncontrado(String recurso) {
        return new ExcecaoNegocio(Response.Status.NOT_FOUND, "nao_encontrado", "Não foi encontrado: " + recurso + ".");
    }
}
