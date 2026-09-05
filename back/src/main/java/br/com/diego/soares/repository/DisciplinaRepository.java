package br.com.diego.soares.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import br.com.diego.soares.entity.Disciplina;

@ApplicationScoped
public class DisciplinaRepository implements PanacheRepository<Disciplina> {
}
