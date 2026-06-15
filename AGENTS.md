# Repository Guidelines

## Project Purpose

This repository contains the backend for the Loan Application Simulator, a Spring Boot application that models a simplified personal loan workflow. Use `Docs/loan_application_simulator_project_description.md` as the product source of truth for MVP behavior, user stories, API intent, banking-domain rules, and optional future features.

The MVP focuses on user registration, login, JWT authentication, user/admin roles, loan application submission, calculated loan results, user-owned application views, admin review, status updates, PostgreSQL persistence, Flyway migrations, and clear documentation.

This is a simulator and learning/portfolio project. Do not implement real credit-decision logic or imply that the calculated risk level is a banking-grade decision.

## Project Structure & Architecture

This is a single-module Gradle Spring Boot project targeting Java 21. Application code lives under `src/main/java`, runtime configuration and database resources live under `src/main/resources`, and project notes belong in `Docs/`. Expected test code mirrors production packages under `src/test/java`, with test resources under `src/test/resources`.

Use a clean onion architecture with these lowercase Java packages for new code:

- `domain`: core entities, value objects, enums, and domain rules. Keep this layer free of Spring, JPA, controllers, repositories, and outer-layer DTOs.
- `dal.contracts`: persistence contracts exposed inward to the business layer.
- `dal`: persistence implementation, JPA entities, Spring Data repositories, database adapters, and DAL mappers.
- `bll.contracts`: business service contracts exposed to the app/API layer.
- `bll`: use cases, business services, loan calculations, authorization-aware workflows, and BLL mappers.
- `app`: Spring Boot entry point, REST controllers, security configuration, application configuration, API exception handling, and web concerns.
- `app.dto`: public request/response DTOs for the REST API.

The current project may still contain initial uppercase package folders such as `App`, `BLL`, `DAL`, and `Domain`. Because the architecture is not yet populated, migrate to the lowercase package names above immediately when adding or moving implementation code.

## Layering Rules

Dependencies must point inward:

- `app` depends on `bll.contracts` and public API DTOs.
- `bll` depends on `domain` and `dal.contracts`.
- `dal` depends on `domain` and implements `dal.contracts`.
- `domain` depends on no application, BLL, DAL, Spring, or persistence code.

Prefer interfaces at layer boundaries. Controllers should call BLL contracts rather than repository classes. Business services should call DAL contracts rather than Spring Data repositories directly. Persistence details, JPA annotations, query methods, and database schema assumptions belong in DAL, not in BLL or App.

Do not return JPA entities from controllers. Do not pass public API DTOs directly into DAL. Do not let Spring Security, HTTP, JWT, or database concerns leak into Domain.

## DTOs, Contracts & Mappers

Use DTOs deliberately at boundaries:

- `app.dto` contains public-facing API request and response DTOs. Treat these as external contracts for the Vue frontend and any API clients.
- BLL and DAL may define internal DTOs where useful for layer boundaries, projections, or use-case results.
- Domain should stay focused on entities, value objects, enums, and behavior. Do not create `domain.dto` as a dumping ground for transport shapes.

Use explicit mapper classes where data crosses layers and the conversion is not trivial. Typical mappings include:

- API request DTO to BLL command/input DTO.
- BLL result DTO to API response DTO.
- DAL/JPA persistence model to Domain object or DAL contract DTO.

Keep mapping code boring and local to the boundary it serves. Avoid hidden conversions that make ownership unclear.

## Backend Feature Guidance

Follow the feature scope in `Docs/loan_application_simulator_project_description.md`.

Core backend responsibilities include:

- validating registration, login, and loan application requests;
- hashing passwords;
- issuing and validating JWT access tokens;
- enforcing `USER` and `ADMIN` authorization rules on the backend;
- ensuring users can only access their own applications;
- calculating estimated monthly payment, available monthly income, debt-to-income ratio, and simplified risk level;
- assigning new applications the initial `PENDING` status;
- allowing admins to view applications and update status to `PENDING`, `APPROVED`, or `REJECTED`;
- persisting data in PostgreSQL through DAL and Flyway-managed schema changes.

The backend must never trust the frontend for security-related decisions or calculated business results.

## Build, Test & Development Commands

Use the checked-in Gradle wrapper instead of assuming a system Gradle installation.

- `.\gradlew.bat clean build`: compile, run tests, and produce build outputs in `build/`.
- `.\gradlew.bat test`: run the JUnit Platform test suite.
- `.\gradlew.bat bootRun`: start the Spring Boot application locally.

On Unix-like shells, use `./gradlew` with the same tasks.

## Coding Style & Naming Conventions

Use standard Java formatting with 4-space indentation and one public top-level class per file. Class names use `PascalCase`; methods, fields, and local variables use `camelCase`; constants use `UPPER_SNAKE_CASE`.

Use lowercase package names for all new code. Keep names explicit and domain-oriented, such as `LoanApplication`, `ApplicationStatus`, `RiskLevel`, `LoanApplicationService`, `LoanApplicationController`, and `LoanApplicationRepository`.

Prefer Spring idioms already represented by the dependencies: controllers for REST endpoints, services for business logic, repositories for persistence, DTOs for request/response boundaries, Bean Validation annotations for input constraints, and security configuration for authentication/authorization. Use Lombok only where it reduces boilerplate clearly without hiding important behavior.

## Testing Guidelines

Tests use `spring-boot-starter-test`, JUnit Platform, and `spring-security-test`. Name unit tests after the class under test, for example `LoanApplicationServiceTest`, and use `*IntegrationTest` for Spring context, database, or security-flow tests. Reserve `@SpringBootTest` for cases that need the full application context.

Run `.\gradlew.bat test` before submitting changes. Add focused tests for:

- registration validation and duplicate email behavior;
- password hashing and login failures;
- JWT-protected endpoint behavior;
- user ownership rules for loan applications;
- admin-only application review and status updates;
- repository queries and Flyway-backed persistence behavior;
- monthly payment, debt-to-income, and risk-level calculation rules.

## Database & Configuration

Use Flyway migrations under `src/main/resources/db/migration` for schema changes. Keep database structure versioned in Git and aligned with the project description tables: `app_users`, `loan_applications`, and `refresh_tokens` if refresh tokens are implemented.

Do not commit secrets, database credentials, JWT signing keys, or local environment files. Keep sensitive values externalized through environment variables or deployment-specific configuration.

## Commit & Pull Request Guidelines

The current history only contains `init`, so no detailed commit convention is established yet. Use short, imperative commit subjects such as `Add loan application validation` or `Configure PostgreSQL migrations`.

Before opening a pull request, run the relevant Gradle tests and summarize any security, database, or API-contract changes. Include notes when a change affects the frontend-facing DTOs or endpoints described in the project document.
