package br.com.diego.soares.service;

import br.com.diego.soares.exception.BusinessException;
import jakarta.enterprise.context.ApplicationScoped;
import br.com.diego.soares.enums.PeriodoEnum;
import jakarta.transaction.Transactional;
import br.com.diego.soares.repository.*;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import br.com.diego.soares.entity.*;
import java.util.stream.Collectors;
import jakarta.inject.Inject;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class MatrizCurricularService {
    @Inject MatrizCurricularRepository matrizRepository;
    @Inject CoordenadorRepository coordenadorRepository;
    @Inject DisciplinaRepository disciplinaRepository;
    @Inject ProfessorRepository professorRepository;
    @Inject HorarioRepository horarioRepository;
    @Inject CursoRepository cursoRepository;
    @Inject MatriculaRepository matriculaRepository;

    @Transactional
    public MatrizCurricular criar(MatrizCurricular dados, String keycloakId) {
        Coordenador coordenador = buscarCoordenador(keycloakId);

        Disciplina disciplina = disciplinaRepository.findByIdOptional(dados.getDisciplina().getId())
                .orElseThrow(() -> new NotFoundException("Disciplina não encontrada"));

        Professor professor = professorRepository.findByIdOptional(dados.getProfessor().getId())
                .orElseThrow(() -> new NotFoundException("Professor não encontrado"));

        Horario horario = horarioRepository.findByIdOptional(dados.getHorario().getId())
                .orElseThrow(() -> new NotFoundException("Horário não encontrado"));

        List<Long> cursosIds = dados.getCursosAutorizados().stream()
                .map(c -> c.getId()).collect(Collectors.toList());
        List<Curso> cursos = resolverCursos(cursosIds);

        MatrizCurricular matriz = new MatrizCurricular();
        matriz.setDisciplina(disciplina);
        matriz.setProfessor(professor);
        matriz.setHorario(horario);
        matriz.setCursosAutorizados(cursos);
        matriz.setQuantidadeMaximaAlunos(dados.getQuantidadeMaximaAlunos());
        matriz.setCoordenador(coordenador);
        matriz.setAtivo(true);

        matrizRepository.persist(matriz);

        return toResponse(matriz, 0L);
    }
}
