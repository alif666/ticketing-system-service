CREATE TABLE app_metadata (
    id BIGINT NOT NULL AUTO_INCREMENT,
    schema_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uq_app_metadata_schema_name UNIQUE (schema_name)
);

INSERT INTO app_metadata (schema_name) VALUES ('foundation');
