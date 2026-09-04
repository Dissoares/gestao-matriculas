CREATE TABLE horario (
    id          BIGINT  NOT NULL,
    dia_semana  INTEGER NOT NULL,
    hora_inicio TIME    NOT NULL,
    hora_fim    TIME    NOT NULL,
    PRIMARY KEY (id)
);
