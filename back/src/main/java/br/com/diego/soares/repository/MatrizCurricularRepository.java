package br.com.diego.soares.repository;

import br.com.diego.soares.entity.MatrizCurricular;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MatrizCurricularRepository implements PanacheRepository<MatrizCurricular> {
}
