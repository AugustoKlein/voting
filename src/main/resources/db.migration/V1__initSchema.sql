CREATE TABLE pauta
(
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    name             VARCHAR(255) NOT NULL,
    description      VARCHAR(255) NULL,
    status           ENUM('CREATED', 'OPEN', 'CLOSED') default 'CREATED',
    ends_at          DATETIME NULL,
    total_vote_count BIGINT NULL,
    yes_vote_count   BIGINT NULL,
    no_vote_count    BIGINT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE voter
(
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    cpf       VARCHAR(255) NOT NULL,
    voted_yes BOOLEAN      NOT NULL,
    pauta_id  BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (pauta_id) REFERENCES pauta (id)
);
