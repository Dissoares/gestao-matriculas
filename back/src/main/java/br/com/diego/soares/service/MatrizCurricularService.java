package br.com.diego.soares.service;

import br.com.diego.soares.entity.*;
import br.com.diego.soares.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MatrizCurricularService {

    @Inject MatrizCurricularRepository matrizRepository;
    @Inject CoordenadorRepository coordenadorRepository;
    @Inject DisciplinaRepository disciplinaRepository;
    @Inject ProfessorRepository professorRepository;
    @Inject HorarioRepository horarioRepository;
    @Inject CursoRepository cursoRepository;

    @Transactional
    public MatrizCurricular criar(MatrizCurricular dados, String keycloakId) {
        Coordenador coordenador = coordenadorRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("Coordenador não encontrado"));

        Disciplina disciplina = disciplinaRepository.findByIdOptional(dados.getDisciplina().getId())
                .orElseThrow(() -> new NotFoundException("Disciplina não encontrada"));

        Professor professor = professorRepository.findByIdOptional(dados.getProfessor().getId())
                .orElseThrow(() -> new NotFoundException("Professor não encontrado"));

        Horario horario = horarioRepository.findByIdOptional(dados.getHorario().getId())
                .orElseThrow(() -> new NotFoundException("Horário não encontrado"));

        List<Long> cursosIds = dados.getCursosAutorizados().stream()
                .map(c -> c.getId()).collect(Collectors.toList());

        List<Curso> cursos = cursosIds.stream()
                .map(id -> cursoRepository.findByIdOptional(id)
                        .orElseThrow(() -> new NotFoundException("Curso " + id + " não encontrado")))
                .collect(Collectors.toList());

        MatrizCurricular matriz = new MatrizCurricular();
        matriz.setDisciplina(disciplina);
        matriz.setProfessor(professor);
        matriz.setHorario(horario);
        matriz.setCursosAutorizados(cursos);
        matriz.setQuantidadeMaximaAlunos(dados.getQuantidadeMaximaAlunos());
        matriz.setCoordenador(coordenador);
        matriz.setAtivo(true);

        matrizRepository.persist(matriz);

        // Inicializa lazy para serialização
        matriz.getDisciplina().getNome();
        matriz.getProfessor().getNome();
        matriz.getHorario().getDiaSemana();
        matriz.getCoordenador().getNome();
        matriz.getCursosAutorizados().size();

        return matriz;
    }
}
