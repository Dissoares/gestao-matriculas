CREATE TABLE matricula (
    id                  BIGINT    NOT NULL,
    aluno_id            BIGINT    NOT NULL,
    matriz_curricular_id BIGINT   NOT NULL,
    data_matricula      TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_matricula_aluno  FOREIGN KEY (aluno_id)             REFERENCES aluno(id),
    CONSTRAINT fk_matricula_matriz FOREIGN KEY (matriz_curricular_id) REFERENCES matriz_curricular(id)
);
