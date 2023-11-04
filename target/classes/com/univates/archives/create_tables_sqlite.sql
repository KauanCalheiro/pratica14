CREATE TABLE usuario
(
    id      INTEGER     NOT NULL PRIMARY KEY AUTOINCREMENT,
    nome    TEXT        NOT NULL,
    cpf     VARCHAR(11) NOT NULL UNIQUE,
    senha   TEXT        NOT NULL,
    salario DOUBLE PRECISION
);

CREATE TABLE transacao
(
    id          INTEGER          NOT NULL PRIMARY KEY AUTOINCREMENT,
    valor       DOUBLE PRECISION NOT NULL,
    data        TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ref_usuario BIGINT           NOT NULL REFERENCES usuario (id)
);
