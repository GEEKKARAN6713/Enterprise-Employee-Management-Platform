# Employee Management Platform

Enterprise-grade employee management system built with **Java 21**, **Spring Boot 3.4**, **Spring Security 6**, **MySQL**, and **Thymeleaf**.

## Quick Start

### Prerequisites

- JDK 21+
- Maven 3.9+
- Docker (optional)

### Run with Docker Compose

```bash
mvn clean package -DskipTests
docker compose up --build
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Admin dashboard: http://localhost:8080/admin/login

**Default admin:** `admin@enterprise.com` / `Admin123!`

### Local development

```bash
# Start MySQL (or use docker compose up mysql)
mvn spring-boot:run
```

Environment variables: `DB_HOST`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET` (see `application.yml`).

## Architecture

Clean layered architecture:

```
Controller → Service → Repository → Database
     ↓
   DTO ↔ MapStruct ↔ Entity
```

| Package | Responsibility |
|---------|----------------|
| `controller` | REST API and Thymeleaf admin MVC |
| `service` | Business logic, transactions, audit hooks |
| `repository` | Spring Data JPA persistence |
| `domain.entity` | JPA entities |
| `domain.enums` | Domain enumerations |
| `dto` | Request/response contracts with Jakarta Validation |
| `mapper` | MapStruct entity ↔ DTO mapping |
| `security` | JWT filter, UserDetails, token provider |
| `config` | Security, OpenAPI, bootstrap |
| `exception` | `@ControllerAdvice` global error handling |
| `validation` | Custom validators (`ValidPassword`, `ValidDateRange`) |

## Security

- **JWT** access tokens (stateless API) + **refresh tokens** stored on `User`
- **BCrypt** password hashing
- **RBAC** roles: `ADMIN`, `MANAGER`, `EMPLOYEE`
- Public: `/auth/login`, `/auth/register`, `/auth/refresh`, `/swagger-ui/**`
- Admin UI: separate filter chain with **form login** at `/admin/**`

## API Overview

| Endpoint | Roles | Description |
|----------|-------|-------------|
| `POST /auth/register` | Public | Register user + profile |
| `POST /auth/login` | Public | Obtain tokens |
| `POST /auth/refresh` | Public | Rotate refresh token |
| `GET/PUT /api/profile` | Authenticated | Own profile |
| `/api/departments/**` | Mixed | Department CRUD |
| `/api/employees/**` | ADMIN, MANAGER | Employee CRUD |
| `/api/leaves/**` | All (workflow) | Leave submit/review/cancel |
| `/api/tasks/**` | MANAGER+ assign | Task workflow |
| `/api/audit-logs` | ADMIN | Audit trail |

Full documentation: **Swagger UI** after startup.

## Database

- **Flyway** migrations in `src/main/resources/db/migration/`
- **Hibernate** `ddl-auto: validate` (schema owned by Flyway)
- Indexing notes: [docs/INDEXING.md](docs/INDEXING.md)

## Testing

```bash
mvn test
```

- Unit tests: `AuthServiceTest`, `DepartmentServiceTest` (Mockito)
- Integration: `AuthControllerIntegrationTest` (H2 + Flyway, `@ActiveProfiles("test")`)

## CI/CD

GitHub Actions workflow: `.github/workflows/ci.yml` — `mvn verify` on push/PR.

## Project Structure

```
src/main/java/com/enterprise/empmgmt/
├── EmployeeManagementApplication.java
├── config/          # Security, OpenAPI, admin bootstrap
├── controller/      # REST + Admin MVC
├── domain/          # Entities and enums
├── dto/             # Request/response DTOs
├── exception/       # Global exception handler
├── mapper/          # MapStruct mappers
├── repository/      # JPA repositories
├── security/        # JWT and UserDetails
├── service/         # Business services
└── validation/      # Custom validators
```

## License

Proprietary — internal enterprise use.
