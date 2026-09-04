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
@Table(name = "disciplina")
@Getter
@Setter
@NoArgsConstructor
public class Disciplina extends PanacheEntity {

    @NotBlank
    @Column(nullable = false)
    private String nome;
}
