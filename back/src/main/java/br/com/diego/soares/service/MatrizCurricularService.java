package br.com.diego.soares.service;

import br.com.diego.soares.repository.MatrizCurricularRepository;
import br.com.diego.soares.repository.CoordenadorRepository;
import br.com.diego.soares.repository.DisciplinaRepository;
import br.com.diego.soares.repository.MatriculaRepository;
import br.com.diego.soares.repository.ProfessorRepository;
import br.com.diego.soares.dto.ReferenciasMatrizResposta;
import br.com.diego.soares.dto.RequisicaoAtualizarMatriz;
import br.com.diego.soares.repository.HorarioRepository;
import br.com.diego.soares.repository.CursoRepository;
import br.com.diego.soares.dto.RequisicaoCriarMatriz;
import br.com.diego.soares.exception.ExcecaoNegocio;
import jakarta.enterprise.context.ApplicationScoped;
import br.com.diego.soares.entity.MatrizCurricular;
import br.com.diego.soares.dto.HorarioResposta;
import br.com.diego.soares.dto.IdNomeResposta;
import br.com.diego.soares.dto.MatrizResposta;
import br.com.diego.soares.entity.Coordenador;
import br.com.diego.soares.entity.Disciplina;
import br.com.diego.soares.enums.PeriodoEnum;
import br.com.diego.soares.entity.Matricula;
import br.com.diego.soares.entity.Professor;
import br.com.diego.soares.entity.Horario;
import jakarta.transaction.Transactional;
import br.com.diego.soares.entity.Curso;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;

import java.util.Collection;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class MatrizCurricularService {

    private final MatrizCurricularRepository repositorioMatriz;
    private final CoordenadorRepository repositorioCoordenador;
    private final DisciplinaRepository repositorioDisciplina;
    private final ProfessorRepository repositorioProfessor;
    private final HorarioRepository repositorioHorario;
    private final CursoRepository repositorioCurso;
    private final MatriculaRepository repositorioMatricula;

    @Inject
    public MatrizCurricularService(
            MatrizCurricularRepository repositorioMatriz,
            CoordenadorRepository repositorioCoordenador,
            DisciplinaRepository repositorioDisciplina,
            ProfessorRepository repositorioProfessor,
            HorarioRepository repositorioHorario,
            CursoRepository repositorioCurso,
            MatriculaRepository repositorioMatricula) {
        this.repositorioMatriz = repositorioMatriz;
        this.repositorioCoordenador = repositorioCoordenador;
        this.repositorioDisciplina = repositorioDisciplina;
        this.repositorioProfessor = repositorioProfessor;
        this.repositorioHorario = repositorioHorario;
        this.repositorioCurso = repositorioCurso;
        this.repositorioMatricula = repositorioMatricula;
    }

    @Transactional
    public MatrizResposta criar(RequisicaoCriarMatriz requisicao, String idKeycloak) {
        Coordenador coordenador = obterCoordenador(idKeycloak);
        Disciplina disciplina = obterDisciplina(requisicao.disciplinaId());
        Professor professor = obterProfessor(requisicao.professorId());
        Horario horario = obterHorario(requisicao.horarioId());
        List<Curso> cursos = obterCursos(requisicao.cursosAutorizadosIds());

        validarHorario(horario);
        validarOfertaEmHorarioDiferente(disciplina.id, horario.id, null);

        MatrizCurricular matriz = new MatrizCurricular();
        matriz.setCoordenador(coordenador);
        matriz.setDisciplina(disciplina);
        matriz.setProfessor(professor);
        matriz.setHorario(horario);
        matriz.setCursosAutorizados(cursos);
        matriz.setQuantidadeMaximaAlunos(requisicao.quantidadeMaximaAlunos());
        matriz.setAtivo(true);
        repositorioMatriz.persist(matriz);

        return MatrizResposta.de(matriz, 0);
    }

    @Transactional
    public List<MatrizResposta> listar(
            String idKeycloak,
            LocalTime horaInicio,
            LocalTime horaFim,
            Integer periodoCodigo,
            Long cursoId,
            Integer quantidadeMaxima) {
        if (horaInicio != null && horaFim != null && !horaInicio.isBefore(horaFim)) {
            throw new ExcecaoNegocio("O horário inicial do filtro deve ser menor que o horário final.");
        }
        if (quantidadeMaxima != null && quantidadeMaxima <= 0) {
            throw new ExcecaoNegocio("A quantidade máxima do filtro deve ser positiva.");
        }

        PeriodoEnum periodo = periodoCodigo == null ? null : converterPeriodo(periodoCodigo);
        return repositorioMatriz.buscarAtivasDoCoordenador(
                        idKeycloak, horaInicio, horaFim, periodo, cursoId, quantidadeMaxima)
                .stream()
                .map(matriz -> MatrizResposta.de(matriz, repositorioMatricula.contarPorIdMatriz(matriz.getId())))
                .toList();
    }

    @Transactional
    public MatrizResposta buscarPorId(Long idMatriz, String idKeycloak) {
        MatrizCurricular matriz = obterMatrizDoCoordenador(idMatriz, idKeycloak);
        return MatrizResposta.de(matriz, repositorioMatricula.contarPorIdMatriz(idMatriz));
    }

    @Transactional
    public MatrizResposta atualizar(RequisicaoAtualizarMatriz requisicao, Long idMatriz, String idKeycloak) {
        MatrizCurricular matriz = obterMatrizDoCoordenador(idMatriz, idKeycloak);
        Professor professor = obterProfessor(requisicao.professorId());
        Horario horario = obterHorario(requisicao.horarioId());
        List<Curso> cursos = obterCursos(requisicao.cursosAutorizadosIds());

        validarHorario(horario);
        validarOfertaEmHorarioDiferente(matriz.getDisciplina().id, horario.id, idMatriz);
        validarCursosMantemAlunosMatriculados(idMatriz, cursos);

        matriz.setProfessor(professor);
        matriz.setHorario(horario);
        matriz.setCursosAutorizados(cursos);
        return MatrizResposta.de(matriz, repositorioMatricula.contarPorIdMatriz(idMatriz));
    }

    @Transactional
    public void excluir(Long idMatriz, String idKeycloak) {
        MatrizCurricular matriz = obterMatrizDoCoordenador(idMatriz, idKeycloak);
        if (repositorioMatricula.contarPorIdMatriz(idMatriz) > 0) {
            throw new ExcecaoNegocio("Não é possível excluir uma aula que possui alunos matriculados.");
        }
        matriz.setAtivo(false);
    }

    @Transactional
    public ReferenciasMatrizResposta listarReferencias() {
        return new ReferenciasMatrizResposta(
                repositorioDisciplina.list("id").stream().map(d -> new IdNomeResposta(d.id, d.getNome())).toList(),
                repositorioProfessor.list("id").stream().map(p -> new IdNomeResposta(p.id, p.getNome())).toList(),
                repositorioHorario.list("id").stream().map(h -> new HorarioResposta(
                        h.id, h.getDiaSemana(), h.getHoraInicio(), h.getHoraFim())).toList(),
                repositorioCurso.list("id").stream().map(c -> new IdNomeResposta(c.id, c.getNome())).toList());
    }

    private Coordenador obterCoordenador(String idKeycloak) {
        return repositorioCoordenador.buscarPorIdKeycloak(idKeycloak)
                .orElseThrow(() -> naoEncontrado("coordenador"));
    }

    private MatrizCurricular obterMatrizDoCoordenador(Long idMatriz, String idKeycloak) {
        MatrizCurricular matriz = repositorioMatriz.buscarAtivaDoCoordenadorPorId(idMatriz, idKeycloak)
                .orElseThrow(() -> naoEncontrado("aula da matriz curricular"));
        matriz.getCursosAutorizados().size();
        return matriz;
    }

    private Disciplina obterDisciplina(Long idDisciplina) {
        return repositorioDisciplina.findByIdOptional(idDisciplina).orElseThrow(() -> naoEncontrado("disciplina"));
    }

    private Professor obterProfessor(Long idProfessor) {
        return repositorioProfessor.findByIdOptional(idProfessor).orElseThrow(() -> naoEncontrado("professor"));
    }

    private Horario obterHorario(Long idHorario) {
        return repositorioHorario.findByIdOptional(idHorario).orElseThrow(() -> naoEncontrado("horário"));
    }

    private List<Curso> obterCursos(Set<Long> idsCursos) {
        return idsCursos.stream()
                .map(idCurso -> repositorioCurso.findByIdOptional(idCurso).orElseThrow(() -> naoEncontrado("curso " + idCurso)))
                .toList();
    }

    private void validarHorario(Horario horario) {
        if (!horario.getHoraInicio().isBefore(horario.getHoraFim())) {
            throw new ExcecaoNegocio("O horário inicial deve ser menor que o horário final.");
        }
    }

    private void validarOfertaEmHorarioDiferente(Long disciplinaId, Long horarioId, Long matrizIdIgnorada) {
        if (repositorioMatriz.existeOfertaAtivaDaDisciplinaNoHorario(disciplinaId, horarioId, matrizIdIgnorada)) {
            throw new ExcecaoNegocio("Esta disciplina já possui uma oferta ativa neste horário.");
        }
    }

    private void validarCursosMantemAlunosMatriculados(Long matrizId, Collection<Curso> cursosAutorizados) {
        Set<Long> idsCursos = cursosAutorizados.stream().map(curso -> curso.id).collect(Collectors.toSet());
        boolean removeriaAlunoMatriculado = repositorioMatricula.buscarPorIdMatriz(matrizId).stream()
                .map(Matricula::getAluno)
                .map(aluno -> aluno.getCurso().id)
                .anyMatch(idCurso -> !idsCursos.contains(idCurso));
        if (removeriaAlunoMatriculado) {
            throw new ExcecaoNegocio("A alteração removeria o curso de um aluno já matriculado.");
        }
    }

    private PeriodoEnum converterPeriodo(Integer codigo) {
        try {
            return PeriodoEnum.fromCodigo(codigo);
        } catch (IllegalArgumentException excecao) {
            throw new ExcecaoNegocio("Período inválido. Use 1 para manhã, 2 para tarde ou 3 para noite.");
        }
    }

    private ExcecaoNegocio naoEncontrado(String recurso) {
        return new ExcecaoNegocio(Response.Status.NOT_FOUND, "nao_encontrado", "Não foi encontrado: " + recurso + ".");
    }
}
