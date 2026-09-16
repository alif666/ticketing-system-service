# Ticketing System Service

Foundation for the Pridesys internal ticketing system take-home assignment.

## Requirements

- Docker Desktop with Compose
- Java 21 and Maven are only needed for local development outside Docker

## Run

From this repository:

```bash
docker compose up --build
```

The API is available at `http://localhost:8080`. The frontend is intentionally not part of this increment; it will be added when the client repository is ready.

For IntelliJ development, start only the database in the background and then run the Spring Boot configuration from IntelliJ:

```bash
docker compose up -d mysql
```

The MySQL container is configured with `restart: unless-stopped`, so Docker Desktop can restart it automatically. The first startup may take up to a minute; wait until `docker compose ps` reports `healthy`. Flyway retries during this startup window.

## Foundation checks

```bash
curl http://localhost:8080/api/health
curl http://localhost:8080/actuator/health
```

You can import `postman-foundation.collection.json` into Postman and run the two requests in order. The collection uses `http://localhost:8080` as its `baseUrl` variable.

Expected responses include `{"status":"UP"}`. Flyway creates the initial `app_metadata` table when the API starts. MySQL data and the private attachment directory are stored in named Docker volumes.

Stop with `Ctrl+C`. A later `docker compose up` reuses the volumes. Do not use `docker compose down -v` unless you intentionally want to remove local data.

## Architecture decisions

- Spring Boot 3.5.x, Java 21, Maven, and REST endpoints.
- MySQL is the relational store selected for the assignment.
- Flyway owns schema migrations from the beginning.
- `/app/storage` is a private filesystem volume behind a storage abstraction that will be introduced with the attachment feature. It is not exposed as a public web directory.
- Authentication, roles, DTOs, validation, and ticket domains are deliberately deferred to later incremental branches.
