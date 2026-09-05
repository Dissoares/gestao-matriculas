package br.com.diego.soares.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coordenador")
@Getter
@Setter
@NoArgsConstructor
public class Coordenador extends PanacheEntity {

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(name = "keycloak_id", nullable = false, unique = true)
    private String keycloakId;
}
