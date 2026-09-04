CREATE TABLE aluno (
    id          BIGINT       NOT NULL,
    nome        VARCHAR(255) NOT NULL,
    keycloak_id VARCHAR(255) NOT NULL,
    curso_id    BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_aluno_keycloak_id UNIQUE (keycloak_id),
    CONSTRAINT fk_aluno_curso FOREIGN KEY (curso_id) REFERENCES curso(id)
);
