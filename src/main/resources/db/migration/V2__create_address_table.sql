-- V2__create_address_table.sql
-- Criação da tabela de endereços (relacionada a users)
-- Autor: Felipe (Tech Lead)
-- Data: Abril 2026

CREATE TABLE address (
    id          VARCHAR(36)  NOT NULL,
    user_id     VARCHAR(36)  NOT NULL,
    rua         VARCHAR(255) NOT NULL,
    numero      VARCHAR(20)  NOT NULL,
    cidade      VARCHAR(100) NOT NULL,
    estado      CHAR(2)      NOT NULL,
    cep         VARCHAR(9)   NOT NULL,
    complemento VARCHAR(255),

    CONSTRAINT pk_address PRIMARY KEY (id),
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_address_user_id ON address (user_id);

