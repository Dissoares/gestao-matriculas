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
@Table(name = "curso")
@Getter
@Setter
@NoArgsConstructor
public class Curso extends PanacheEntity {

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;
}
