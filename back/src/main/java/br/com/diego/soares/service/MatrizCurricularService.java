package br.com.diego.soares.service;

import br.com.diego.soares.dto.MatrizCurricularEditRequest;
import br.com.diego.soares.dto.MatrizCurricularResponse;
import br.com.diego.soares.dto.MatrizCurricularRequest;
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
    public MatrizCurricularResponse criar(MatrizCurricularRequest request, String keycloakId) {
        Coordenador coordenador = buscarCoordenador(keycloakId);

        Disciplina disciplina = disciplinaRepository.findByIdOptional(request.getDisciplinaId()).orElseThrow(() -> new NotFoundException("Disciplina não encontrada"));

        Professor professor = professorRepository.findByIdOptional(request.getProfessorId()).orElseThrow(() -> new NotFoundException("Professor não encontrado"));

        Horario horario = horarioRepository.findByIdOptional(request.getHorarioId()).orElseThrow(() -> new NotFoundException("Horário não encontrado"));

        List<Curso> cursos = resolverCursos(request.getCursosAutorizadosIds());

        MatrizCurricular matriz = new MatrizCurricular();
        matriz.setDisciplina(disciplina);
        matriz.setProfessor(professor);
        matriz.setHorario(horario);
        matriz.setCursosAutorizados(cursos);
        matriz.setQuantidadeMaximaAlunos(request.getQuantidadeMaximaAlunos());
        matriz.setCoordenador(coordenador);
        matriz.setAtivo(true);

        matrizRepository.persist(matriz);

        return toResponse(matriz, 0L);
    }
}
