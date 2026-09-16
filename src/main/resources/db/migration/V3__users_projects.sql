CREATE TABLE clients (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(160) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id), CONSTRAINT uq_clients_name UNIQUE (name)
);

ALTER TABLE users ADD COLUMN client_id BIGINT NULL;
ALTER TABLE users ADD CONSTRAINT fk_users_client FOREIGN KEY (client_id) REFERENCES clients (id);

CREATE TABLE projects (
    id BIGINT NOT NULL AUTO_INCREMENT, name VARCHAR(160) NOT NULL, short_code VARCHAR(30) NOT NULL,
    description VARCHAR(1000), active BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id), CONSTRAINT uq_projects_short_code UNIQUE (short_code)
);
CREATE TABLE project_memberships (
    project_id BIGINT NOT NULL, user_id BIGINT NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (project_id,user_id), FOREIGN KEY (project_id) REFERENCES projects(id), FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE modules (
    id BIGINT NOT NULL AUTO_INCREMENT, project_id BIGINT NOT NULL, name VARCHAR(160) NOT NULL,
    description VARCHAR(1000), active BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id), CONSTRAINT uq_modules_project_name UNIQUE(project_id,name), FOREIGN KEY(project_id) REFERENCES projects(id)
);

INSERT INTO clients(name) VALUES ('Acme Corporation'), ('Globex Corporation') ON DUPLICATE KEY UPDATE name=VALUES(name);
UPDATE users SET client_id=(SELECT id FROM clients WHERE name='Acme Corporation') WHERE email IN ('client.admin@example.com','client.user@example.com');
