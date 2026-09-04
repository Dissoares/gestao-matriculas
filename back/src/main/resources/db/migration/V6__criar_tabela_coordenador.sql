CREATE TABLE coordenador (
    id          BIGINT       NOT NULL,
    nome        VARCHAR(255) NOT NULL,
    keycloak_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_coordenador_keycloak_id UNIQUE (keycloak_id)
);
