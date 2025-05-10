CREATE TABLE IF NOT EXISTS tb_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    uuid VARCHAR(36) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT
);

CREATE TABLE IF NOT EXISTS tb_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    uuid VARCHAR(36) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(320) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    salt VARCHAR(255) NOT NULL,
    hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    FOREIGN KEY (role_id) REFERENCES tb_role(id)
);

CREATE TABLE IF NOT EXISTS tb_financial_entity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    uuid VARCHAR(36) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    FOREIGN KEY (user_id) REFERENCES tb_user (id)
);

CREATE TABLE IF NOT EXISTS tb_payment (
    id BIGINT PRIMARY KEY,
    payee VARCHAR(255) NOT NULL UNIQUE,
    FOREIGN KEY (id) REFERENCES tb_financial_entity (id)
);

CREATE TABLE IF NOT EXISTS tb_receipt (
    id BIGINT PRIMARY KEY,
    vendor VARCHAR(255) NOT NULL UNIQUE,
    FOREIGN KEY (id) REFERENCES tb_financial_entity (id)
);

CREATE TABLE IF NOT EXISTS tb_recurring_payment (
    id BIGINT PRIMARY KEY,
    payee VARCHAR(255) NOT NULL UNIQUE,
    FOREIGN KEY (id) REFERENCES tb_financial_entity (id)
);

CREATE TABLE IF NOT EXISTS tb_recurring_receipt (
    id BIGINT PRIMARY KEY,
    vendor VARCHAR(255) NOT NULL UNIQUE,
    FOREIGN KEY (id) REFERENCES tb_financial_entity (id)
);

CREATE TABLE IF NOT EXISTS tb_recurrence (
    id BIGINT PRIMARY KEY,
    recurring_until DATE NOT NULL,
    FOREIGN KEY (id) REFERENCES tb_financial_entity (id)
);