package br.com.diego.soares.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import br.com.diego.soares.entity.Coordenador;
import java.util.Optional;

@ApplicationScoped
public class CoordenadorRepository implements PanacheRepository<Coordenador> {

    public Optional<Coordenador> buscarPorIdKeycloak(String idKeycloak) {
        return find("keycloakId", idKeycloak).firstResultOptional();
    }
}
