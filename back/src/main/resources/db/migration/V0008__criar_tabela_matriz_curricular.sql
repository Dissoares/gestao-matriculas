CREATE TABLE matriz_curricular (
    id                      BIGINT  NOT NULL,
    disciplina_id           BIGINT  NOT NULL,
    professor_id            BIGINT  NOT NULL,
    horario_id              BIGINT  NOT NULL,
    quantidade_maxima_alunos INTEGER NOT NULL,
    coordenador_id          BIGINT  NOT NULL,
    ativo                   BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_matriz_disciplina  FOREIGN KEY (disciplina_id)  REFERENCES disciplina(id),
    CONSTRAINT fk_matriz_professor   FOREIGN KEY (professor_id)   REFERENCES professor(id),
    CONSTRAINT fk_matriz_horario     FOREIGN KEY (horario_id)     REFERENCES horario(id),
    CONSTRAINT fk_matriz_coordenador FOREIGN KEY (coordenador_id) REFERENCES coordenador(id)
);

CREATE TABLE matriz_curso (
    matriz_id BIGINT NOT NULL,
    curso_id  BIGINT NOT NULL,
    PRIMARY KEY (matriz_id, curso_id),
    CONSTRAINT fk_matriz_curso_matriz FOREIGN KEY (matriz_id) REFERENCES matriz_curricular(id),
    CONSTRAINT fk_matriz_curso_curso  FOREIGN KEY (curso_id)  REFERENCES curso(id)
);
