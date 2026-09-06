package br.com.diego.soares.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "matriz_curricular")
@Getter
@Setter
@NoArgsConstructor
public class MatrizCurricular extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "matriz_curricular_seq")
    @SequenceGenerator(name = "matriz_curricular_seq", sequenceName = "matriz_curricular_seq", allocationSize = 50)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "matriz_curso",
        joinColumns = @JoinColumn(name = "matriz_id"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private List<Curso> cursosAutorizados = new ArrayList<>();

    @NotNull
    @Positive
    @Column(name = "quantidade_maxima_alunos", nullable = false)
    private Integer quantidadeMaximaAlunos;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordenador_id", nullable = false)
    private Coordenador coordenador;

    @Column(nullable = false)
    private boolean ativo = true;
}
