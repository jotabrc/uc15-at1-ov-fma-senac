CREATE TABLE IF NOT EXISTS tb_role (
    uuid VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT
);

CREATE TABLE IF NOT EXISTS tb_user (
    uuid VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(320) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    role_uuid VARCHAR(36) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    FOREIGN KEY (role_uuid) REFERENCES tb_role(uuid)
);

CREATE TABLE IF NOT EXISTS tb_user_finance (
    uuid VARCHAR(36) PRIMARY KEY,
    user_uuid VARCHAR(36) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    FOREIGN KEY (user_uuid) REFERENCES tb_user (uuid)
);

CREATE TABLE IF NOT EXISTS tb_financial_entity (
    uuid VARCHAR(36) PRIMARY KEY,
    user_finance_uuid VARCHAR(36) NOT NULL,
    due_date DATE NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    FOREIGN KEY (user_finance_uuid) REFERENCES tb_user_finance (uuid)
);

CREATE TABLE IF NOT EXISTS tb_payment (
    uuid VARCHAR(36) PRIMARY KEY,
    payee VARCHAR(255) NOT NULL,
    FOREIGN KEY (uuid) REFERENCES tb_financial_entity (uuid)
);

CREATE TABLE IF NOT EXISTS tb_receipt (
    uuid VARCHAR(36) PRIMARY KEY,
    vendor VARCHAR(255) NOT NULL,
    FOREIGN KEY (uuid) REFERENCES tb_financial_entity (uuid)
);

CREATE TABLE IF NOT EXISTS tb_recurring_payment (
    uuid VARCHAR(36) PRIMARY KEY,
    payee VARCHAR(255) NOT NULL,
    FOREIGN KEY (uuid) REFERENCES tb_financial_entity (uuid)
);

CREATE TABLE IF NOT EXISTS tb_recurring_receipt (
    uuid VARCHAR(36) PRIMARY KEY,
    vendor VARCHAR(255) NOT NULL,
    FOREIGN KEY (uuid) REFERENCES tb_financial_entity (uuid)
);

CREATE TABLE IF NOT EXISTS tb_recurrence (
    uuid VARCHAR(36) PRIMARY KEY,
    recurring_until DATE NOT NULL,
    FOREIGN KEY (uuid) REFERENCES tb_financial_entity (uuid)
);