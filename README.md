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

You can import `postman.json` into Postman and run the Foundation requests. The collection uses `http://localhost:8080` as its `baseUrl` variable and will grow with each incremental API update.

Expected responses include `{"status":"UP"}`. Flyway creates the initial `app_metadata` table when the API starts. MySQL data and the private attachment directory are stored in named Docker volumes.

Stop with `Ctrl+C`. A later `docker compose up` reuses the volumes. Do not use `docker compose down -v` unless you intentionally want to remove local data.

## Architecture decisions

- Spring Boot 3.5.x, Java 21, Maven, and REST endpoints.
- Spring Data JPA repositories are the canonical persistence interfaces (`UserRepository`, `ClientRepository`, `ProjectRepository`, `ModuleRepository`, and `ProjectMembershipRepository`). Record-mapping facades are explicitly named `*RecordRepository`; no `*JpaRepository` naming is used.
- Password reset persistence also uses `ResetTokenRepository extends JpaRepository`; `ResetTokenRecordRepository` preserves the auth service's immutable token contract.
- MySQL is the relational store selected for the assignment.
- Flyway owns schema migrations from the beginning.
- `/app/storage` is a private filesystem volume behind a storage abstraction that will be introduced with the attachment feature. It is not exposed as a public web directory.
- Ticket-domain features are deliberately deferred to later incremental branches.

## Users, clients, projects and modules

The `3-users-projects` increment adds protected `/api/users`, `/api/clients`, `/api/projects`, and nested `/api/projects/{id}/modules` endpoints. APP_ADMIN manages clients/projects/memberships and all users. CLIENT_ADMIN can manage users in its own client (except APP_ADMIN) and modules in assigned projects. CLIENT_USER is read-only for this administration area. Projects are visible to client roles only through `project_memberships`. Use the seeded APP_ADMIN account to create a project, add the seeded client users as members, and then verify scoped access with the client login. Flyway migration `V3__users_projects.sql` creates the client, membership, project, and module tables and associates the seeded client users with Acme Corporation.

The cumulative `postman.json` collection contains requests for the new endpoints. Create a project as APP_ADMIN, add a member using `PUT /api/projects/{projectId}/members/{userId}`, then log in as that member before listing projects/modules.

## Authentication foundation test suite

Run the regression suite with:

```bash
mvn test
```

Tests live under `src/test/java` and cover active/inactive login, wrong passwords, JWT identity and role claims, password changes, and single-use/expired reset tokens. Local seed users use `Password123!`; seed generation hashes this password at startup and can be disabled with `APP_SEED_ENABLED=false`.

The Auth and profile folder in `postman.json` contains login, profile, password, reset, and unauthenticated-access checks. The login request stores its JWT in the collection's `token` variable.

Seed accounts:

| Email | Role | Password |
| --- | --- | --- |
| `app.admin@example.com` | APP_ADMIN | `Password123!` |
| `client.admin@example.com` | CLIENT_ADMIN | `Password123!` |
| `client.user@example.com` | CLIENT_USER | `Password123!` |

Reset tokens are logged by the API with the `[DEV PASSWORD RESET]` prefix. In production, the token must be delivered through an email provider instead of logs.
