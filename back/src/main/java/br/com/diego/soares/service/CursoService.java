package br.com.diego.soares.service;

import br.com.diego.soares.entity.Curso;
import br.com.diego.soares.repository.CursoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CursoService {
    @Inject CursoRepository cursoRepository;

    public List<Curso> listarTudos() {
        return cursoRepository.listAll();
    }
}
