CREATE TABLE usuario 
(
    id      SERIAL           PRIMARY KEY,
    nome    TEXT             NOT NULL,
    cpf     VARCHAR(11)      NOT NULL UNIQUE,
    senha   TEXT             NOT NULL,
    salario DOUBLE PRECISION
);

CREATE TABLE transacao 
(
    id          SERIAL           PRIMARY KEY,
    valor       DOUBLE PRECISION NOT NULL,
    data        TIMESTAMP        NOT NULL DEFAULT NOW(),
    ref_usuario BIGINT           NOT NULL,
    
    CONSTRAINT transacao_ref_usuario_foreign FOREIGN KEY (ref_usuario) REFERENCES usuario (id)
);
