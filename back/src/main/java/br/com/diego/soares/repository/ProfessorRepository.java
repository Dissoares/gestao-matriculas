package br.com.diego.soares.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import br.com.diego.soares.entity.Professor;

@ApplicationScoped
public class ProfessorRepository implements PanacheRepository<Professor> {
}
