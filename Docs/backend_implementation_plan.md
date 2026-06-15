# Backend Implementation Plan

## Purpose

This plan describes how to implement the Spring Boot backend for the Loan Application Simulator MVP. The product source of truth is `Docs/loan_application_simulator_project_description.md`.

The backend must remain a simulator. Risk level is a simplified educational calculation, not a real banking credit decision.

## Current Starting Point

- Java 21 single-module Gradle Spring Boot project.
- Dependencies already include Spring Web MVC, Security, Data JPA, Validation, Flyway, PostgreSQL, Actuator, JJWT, Lombok, and Spring test support.
- Current source code is minimal and still uses an uppercase `App` package.
- `src/main/resources/dbschema.sql` contains an initial schema draft, but Flyway migrations are not yet in place.

## Target Architecture

Use the lowercase package structure required by `AGENTS.md`:

```text
src/main/java/mamarr/loanapplicationapp/
  app/
  app/dto/
  bll/
  bll/contracts/
  dal/
  dal/contracts/
  domain/
```

Layering rules:

- `domain` contains entities, enums, value objects, and pure domain rules.
- `dal.contracts` defines persistence contracts used by BLL.
- `dal` contains JPA entities, Spring Data repositories, adapters, and DAL mappers.
- `bll.contracts` defines service interfaces used by App.
- `bll` contains use cases, authorization-aware workflows, calculations, and BLL mappers.
- `app` contains controllers, security, configuration, exception handling, and the Spring Boot entry point.
- `app.dto` contains public API request and response DTOs.

## Phase 1: Project Foundation

Goals:

- Move `LoanApplicationAppApplication` into `mamarr.loanapplicationapp.app`.
- Ensure component scanning covers all lowercase packages.
- Remove or ignore uppercase package folders after migration.
- Replace `dbschema.sql` with Flyway migrations under `src/main/resources/db/migration`.
- Configure local PostgreSQL properties through environment variables.

Implementation tasks:

- Create package directories for `domain`, `dal.contracts`, `dal`, `bll.contracts`, `bll`, `app`, and `app.dto`.
- Add `V1__create_initial_tables.sql` based on the current schema draft.
- Keep secrets out of `application.properties`; use placeholders such as `${DB_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}`, and `${JWT_SECRET}`.
- Add basic global API error handling in `app`.

Verification:

- `.\gradlew.bat clean build`
- Application context starts without package scanning issues.
- Flyway can create `app_users`, `loan_applications`, and optional `refresh_tokens`.

## Phase 2: Domain Model and Calculation Rules

Goals:

- Implement the core loan simulation model without Spring or persistence dependencies.
- Keep all calculation rules deterministic and testable.

Domain types:

- `AppUser`
- `UserRole` with `USER`, `ADMIN`
- `LoanApplication`
- `ApplicationStatus` with `PENDING`, `APPROVED`, `REJECTED`
- `RiskLevel` with `LOW`, `MEDIUM`, `HIGH`
- Value objects where useful for money/rates, or plain `BigDecimal` with centralized validation if simpler.

Calculation rules:

- New applications always start as `PENDING`.
- Use a fixed simulator interest rate for MVP unless configuration is needed later.
- Estimated monthly payment should be calculated in the backend.
- Available monthly income = `monthlyIncome - monthlyExpenses - existingLiabilities`.
- Debt-to-income ratio should include existing liabilities plus estimated new monthly payment.
- Risk level:
  - below 30% DTI: `LOW`
  - 30% to 50% DTI: `MEDIUM`
  - above 50% DTI: `HIGH`

Verification:

- Unit tests for monthly payment, available income, DTI, and risk boundaries.
- Tests for invalid numeric inputs and status defaults.

## Phase 3: Persistence Layer

Goals:

- Persist users and loan applications in PostgreSQL.
- Keep JPA details inside `dal`.

Implementation tasks:

- Create JPA entities:
  - `UserJpaEntity`
  - `LoanApplicationJpaEntity`
  - `RefreshTokenJpaEntity` only if refresh tokens are included in MVP.
- Create Spring Data repositories:
  - `UserSpringDataRepository`
  - `LoanApplicationSpringDataRepository`
  - `RefreshTokenSpringDataRepository` if needed.
- Create DAL contracts:
  - `UserRepository`
  - `LoanApplicationRepository`
  - `RefreshTokenRepository` if needed.
- Create DAL adapters that implement contracts and map between JPA entities and domain objects.
- Add repository queries for:
  - finding user by email;
  - checking duplicate email;
  - finding loan applications by owner;
  - finding application by ID and owner;
  - listing all applications for admins;
  - filtering by status if implemented in MVP.

Verification:

- Data JPA tests for repositories and adapters.
- Migration-backed test database if available, or Testcontainers later if Docker is introduced.

## Phase 4: Authentication and Authorization

Goals:

- Implement registration, login, JWT authentication, and backend-enforced roles.

Implementation tasks:

- Add API DTOs:
  - `RegisterRequest`
  - `LoginRequest`
  - `AuthResponse`
  - optional `RefreshTokenRequest`
  - optional `LogoutRequest`
- Add BLL contracts:
  - `AuthService`
  - `CurrentUserService` or equivalent user context abstraction.
- Implement registration:
  - validate request;
  - reject duplicate email;
  - hash password using `PasswordEncoder`;
  - save user with `USER` role.
- Implement login:
  - validate credentials;
  - issue JWT access token;
  - return role and token data.
- Implement security:
  - `SecurityConfig`
  - JWT service
  - JWT authentication filter
  - authenticated user principal
  - endpoint authorization rules.
- Seed or document admin user creation. Prefer a controlled development seed path or documented SQL, not hardcoded production credentials.

Endpoint scope:

```http
POST /api/auth/register
POST /api/auth/login
POST /api/auth/refresh   optional
POST /api/auth/logout    optional
```

Verification:

- Registration tests for duplicate email and password hashing.
- Login tests for valid and invalid credentials.
- Security tests proving protected endpoints reject missing/invalid tokens.
- Role tests proving regular users cannot access admin endpoints.

## Phase 5: User Loan Application API

Goals:

- Allow authenticated users to submit and view their own applications.
- Never trust frontend-provided calculated values.

Implementation tasks:

- Add API DTOs:
  - `CreateLoanApplicationRequest`
  - `LoanApplicationResponse`
  - optional `LoanApplicationDetailsResponse`
- Add BLL contracts:
  - `LoanApplicationService`
- Implement user workflows:
  - create application for current user;
  - calculate payment, DTI, available income, and risk;
  - save with `PENDING` status;
  - list current user's applications;
  - get current user's application by ID.
- Ensure all ownership checks happen in BLL or below, not only in controllers.

Endpoint scope:

```http
POST /api/applications
GET /api/applications
GET /api/applications/{id}
```

Verification:

- Service tests for ownership rules.
- Controller/security tests for authenticated access.
- Tests proving users cannot access another user's applications.
- Tests proving frontend-supplied calculated fields are not accepted.

## Phase 6: Admin Review API

Goals:

- Allow admins to review all applications and update status.
- Keep admin actions backend-authorized.

Implementation tasks:

- Add API DTOs:
  - `UpdateApplicationStatusRequest`
  - admin list/detail response if it differs from user response.
- Add BLL workflows:
  - list all applications;
  - get any application by ID;
  - update status to `PENDING`, `APPROVED`, or `REJECTED`.
- Add optional status filtering only after the basic admin list works.

Endpoint scope:

```http
GET /api/admin/applications
GET /api/admin/applications/{id}
PATCH /api/admin/applications/{id}/status
```

Verification:

- Admin-only endpoint tests.
- Status update tests for valid and invalid statuses.
- User dashboard sees updated application status after admin update.

## Phase 7: API Error Handling and Documentation

Goals:

- Make API failures predictable for the Vue frontend.
- Document endpoint contracts clearly.

Implementation tasks:

- Add global exception handling for:
  - validation errors;
  - authentication failures;
  - authorization failures;
  - duplicate email;
  - not found;
  - ownership violations;
  - invalid status transitions or invalid enum values.
- Standardize error response shape.
- Update README or add API documentation in `Docs/`.
- Add OpenAPI/Swagger only if it does not distract from MVP completion.

Verification:

- Controller tests assert response status codes and error body shape.
- Manual API smoke tests with representative requests.

## Phase 8: MVP Hardening

Goals:

- Prepare the backend for frontend integration and portfolio presentation.

Implementation tasks:

- Add CORS configuration for the Vue dev server.
- Add Docker Compose for PostgreSQL if not already present.
- Add sample environment variable documentation.
- Add health endpoint expectations using Actuator.
- Review DTO names and endpoint paths against the product document.
- Confirm no secrets or local credentials are committed.

Verification:

- `.\gradlew.bat clean build`
- `.\gradlew.bat test`
- Manual smoke flow:
  - register user;
  - login;
  - create loan application;
  - list own applications;
  - login as admin;
  - list all applications;
  - update status;
  - verify user sees updated status.

## Recommended Implementation Order

1. Package migration and Flyway migration.
2. Domain enums, entities, and calculation service.
3. DAL contracts, JPA entities, repositories, and adapters.
4. Auth registration and login.
5. JWT filter and endpoint security.
6. User loan application endpoints.
7. Admin application endpoints.
8. Error handling, tests, CORS, and documentation polish.

## Test Coverage Checklist

- Registration validation.
- Duplicate email handling.
- Password hashing.
- Login success and failure.
- JWT-protected endpoint behavior.
- Role-based admin access.
- User ownership for loan applications.
- Loan calculation rules.
- Risk level thresholds.
- New application status defaults to `PENDING`.
- Admin status updates.
- Repository queries and migrations.

## MVP Out of Scope

Avoid these until the core backend works:

- Real credit decisioning.
- External banking integrations.
- Email delivery.
- RabbitMQ events.
- MongoDB audit logging.
- Kubernetes manifests.
- Complex status history.
- Refresh token rotation, unless chosen deliberately as part of auth scope.

## Completion Criteria

The backend MVP is complete when:

- Users can register and log in.
- JWT authentication protects private endpoints.
- Roles are enforced on the backend.
- Users can submit loan applications.
- Calculated loan results are produced by backend code.
- Users can view only their own applications.
- Admins can view all applications and update status.
- PostgreSQL persistence is managed through Flyway.
- Tests cover the main security, calculation, ownership, and persistence behaviors.
- Documentation explains setup, configuration, and API usage.
