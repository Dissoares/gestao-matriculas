package br.com.diego.soares.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import br.com.diego.soares.entity.Aluno;
import java.util.Optional;

@ApplicationScoped
public class AlunoRepository implements PanacheRepository<Aluno> {

    public Optional<Aluno> buscarPorIdKeycloak(String idKeycloak) {
        return find("keycloakId", idKeycloak).firstResultOptional();
    }
}
