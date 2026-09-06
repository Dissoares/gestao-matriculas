package br.com.diego.soares.service;

import br.com.diego.soares.dto.IdNomeResposta;
import br.com.diego.soares.repository.CursoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CursoService {

    private final CursoRepository repositorioCurso;

    @Inject
    public CursoService(CursoRepository repositorioCurso) {
        this.repositorioCurso = repositorioCurso;
    }

    public List<IdNomeResposta> listarTodos() {
        return repositorioCurso.listAll().stream()
                .map(curso -> new IdNomeResposta(curso.id, curso.getNome()))
                .toList();
    }
}
