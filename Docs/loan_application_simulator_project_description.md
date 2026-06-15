# Loan Application Simulator

## Project Overview

**Loan Application Simulator** is a full-stack web application that simulates a simplified personal loan application process. The goal of the project is to demonstrate practical skills in modern web development using a technology stack commonly used in enterprise and financial-sector applications.

The application allows users to register, log in, submit loan applications, view their own applications, and see calculated loan information such as estimated monthly payment, debt-to-income ratio, and risk level. Admin users can review all submitted applications and update their status.

This project is not intended to make real credit decisions. It is a learning and portfolio project that models the basic logic behind a banking loan application workflow.

---

## Main Goal

The main goal of the project is to build a realistic, clean, and understandable full-stack application using:

- Vue 3 frontend
- Java Spring Boot backend
- PostgreSQL database
- JWT-based authentication
- Role-based access control
- Docker-based local development setup

The project should be small enough to complete in approximately one month, but complex enough to demonstrate backend logic, authentication, database design, frontend integration, and business-domain understanding.

---

## Target Users

### Regular User

A regular user can:

- Create an account
- Log in
- Submit a loan application
- View their own loan applications
- View the calculated result of an application
- See the current status of each application

### Admin User

An admin user can:

- Log in
- View all submitted loan applications
- Open application details
- Change application status
- Approve or reject applications

---

## Core Features

### 1. User Registration

The user can create an account by entering:

- First name
- Last name
- Email
- Password

The backend validates the data, checks whether the email is already used, hashes the password, and stores the user in the database.

---

### 2. User Login

The user can log in using email and password.

After successful login, the backend returns an access token. If refresh tokens are implemented, the backend also creates a refresh token and stores its hash in the database.

---

### 3. JWT Authentication

The application uses JWT authentication to protect API endpoints.

The frontend sends the access token with protected requests using the `Authorization` header:

```http
Authorization: Bearer <access-token>
```

The backend validates the token before allowing access to protected resources.

---

### 4. Loan Application Form

The user can submit a loan application with the following information:

- Requested loan amount
- Loan term in months
- Monthly income
- Monthly expenses
- Existing monthly liabilities
- Loan purpose

Example:

```json
{
  "amount": 10000,
  "termMonths": 48,
  "monthlyIncome": 1800,
  "monthlyExpenses": 700,
  "existingLiabilities": 250,
  "purpose": "Car purchase"
}
```

---

### 5. Loan Calculation

When a user submits an application, the backend calculates:

- Estimated monthly payment
- Available monthly income
- Debt-to-income ratio
- Risk level

The calculation should happen in the backend, not only in the frontend.

---

### 6. Risk Level Calculation

The project uses a simplified rule-based risk model.

Example logic:

| Condition | Risk Level |
|---|---|
| Debt-to-income ratio below 30% | LOW |
| Debt-to-income ratio between 30% and 50% | MEDIUM |
| Debt-to-income ratio above 50% | HIGH |

This is only a simulation and does not represent a real banking credit decision.

---

### 7. User Dashboard

The user dashboard shows all applications submitted by the logged-in user.

Each application should display:

- Application ID
- Loan amount
- Loan term
- Monthly payment
- Debt-to-income ratio
- Risk level
- Status
- Creation date

A user must only be able to see their own applications.

---

### 8. Admin Dashboard

The admin dashboard shows all loan applications in the system.

The admin can:

- View all applications
- Filter applications by status
- Open application details
- Change application status

Possible statuses:

- `PENDING`
- `APPROVED`
- `REJECTED`

---

## Banking Domain Explanation

In banking terms, a loan application is a request from a customer to borrow money from the bank. The bank needs enough information to estimate whether the customer can afford the loan and whether the risk is acceptable.

The bank typically looks at:

- Who the customer is
- How much money the customer wants to borrow
- How long the customer wants to borrow it for
- What the customer's monthly income is
- What the customer's regular expenses are
- What existing loan obligations the customer already has
- How large the new monthly payment would be
- How much of the customer's income would go toward debt payments

In this project, the backend simulates this process by calculating a monthly payment and a simplified risk level.

---

## Backend Responsibilities

When a user submits a loan application, the backend should:

1. Check that the user is authenticated
2. Validate the request body
3. Check that numeric values are valid
4. Calculate the estimated monthly payment
5. Calculate the debt-to-income ratio
6. Determine the risk level
7. Assign the initial status `PENDING`
8. Save the application to PostgreSQL
9. Return the saved application to the frontend

The backend must never trust the frontend for security-related decisions.

---

## User Stories

### Authentication

#### User Story 1

As a user, I want to register an account so that I can submit loan applications.

**Acceptance Criteria**

- The user can enter first name, last name, email, and password
- The email must be unique
- The password must be stored hashed
- A new user is saved with the `USER` role

---

#### User Story 2

As a user, I want to log in so that I can access my dashboard.

**Acceptance Criteria**

- The user can log in with valid credentials
- Invalid credentials return an error
- Successful login returns an access token
- Protected endpoints require a valid token

---

### Loan Applications

#### User Story 3

As a user, I want to create a loan application so that I can see my estimated loan result.

**Acceptance Criteria**

- The user can submit loan amount, term, income, expenses, liabilities, and purpose
- The backend validates all fields
- The backend calculates monthly payment and risk level
- The application is saved with status `PENDING`

---

#### User Story 4

As a user, I want to view my loan applications so that I can track their status.

**Acceptance Criteria**

- The user sees only their own applications
- Each application shows amount, term, monthly payment, risk level, and status
- The user cannot access another user's applications

---

### Admin

#### User Story 5

As an admin, I want to see all loan applications so that I can review them.

**Acceptance Criteria**

- Only users with the `ADMIN` role can access the admin dashboard
- The admin can see all applications
- Regular users cannot access admin endpoints

---

#### User Story 6

As an admin, I want to update an application status so that I can approve or reject applications.

**Acceptance Criteria**

- The admin can set status to `PENDING`, `APPROVED`, or `REJECTED`
- The updated status is saved in the database
- The user can see the updated status in their dashboard

---

## Technology Stack

## Frontend

### Vue 3

Vue 3 is used to build the single-page frontend application. It is suitable for creating interactive dashboards, forms, and protected user views.

Used for:

- Login page
- Register page
- User dashboard
- Loan application form
- Application detail view
- Admin dashboard

---

### TypeScript

TypeScript is used to make frontend code safer and easier to maintain.

Used for:

- API response types
- Form data types
- Component props
- Authentication state

---

### Vue Router

Vue Router is used for frontend navigation.

Example routes:

- `/login`
- `/register`
- `/dashboard`
- `/applications/new`
- `/applications/:id`
- `/admin/applications`

---

### Pinia

Pinia is used for frontend state management.

Used for:

- Authentication state
- Logged-in user information
- Token handling
- Role checking
- Logout logic

---

### Axios

Axios is used to communicate with the backend API.

Used for:

- Login requests
- Register requests
- Loan application requests
- Admin requests

---

## Backend

### Java 21

Java is used for the backend because it is stable, strongly typed, and widely used in enterprise and financial-sector systems.

---

### Spring Boot

Spring Boot is used to build the REST API and backend business logic.

Used for:

- REST controllers
- Service layer
- Security configuration
- Database access
- Validation
- Application configuration

---

### Spring Web MVC

Spring Web MVC is used to create REST endpoints for the frontend.

Example endpoints:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/refresh`
- `GET /api/applications`
- `POST /api/applications`
- `GET /api/admin/applications`
- `PATCH /api/admin/applications/{id}/status`

---

### Spring Security

Spring Security is used for authentication and authorization.

Used for:

- Login flow
- Password hashing
- JWT authentication filter
- Protected endpoints
- Role-based access control

---

### Spring Data JPA

Spring Data JPA is used to communicate with PostgreSQL using entities and repositories.

Used for:

- User repository
- Loan application repository
- Refresh token repository

---

### Bean Validation

Validation is used to check request bodies.

Example annotations:

- `@NotBlank`
- `@Email`
- `@Positive`
- `@Min`
- `@Max`

---

### Flyway

Flyway is used for database migrations.

Used for:

- Creating tables
- Updating schema over time
- Keeping database structure versioned in Git

---

## Database

### PostgreSQL

PostgreSQL is used as the relational database.

It stores:

- Users
- Loan applications
- Refresh tokens, if implemented

PostgreSQL is a good fit because the data is structured and has clear relationships.

---

## Recommended Database Tables

### `app_users`

Stores registered users.

| Column | Type | Notes |
|---|---|---|
| `id` | UUID | Primary key |
| `email` | VARCHAR | Unique |
| `password_hash` | VARCHAR | Hashed password |
| `first_name` | VARCHAR | User first name |
| `last_name` | VARCHAR | User last name |
| `role` | VARCHAR | `USER` or `ADMIN` |
| `created_at` | TIMESTAMP | Created time |
| `updated_at` | TIMESTAMP | Updated time |

---

### `loan_applications`

Stores loan applications.

| Column | Type | Notes |
|---|---|---|
| `id` | UUID | Primary key |
| `user_id` | UUID | Foreign key to `app_users` |
| `amount` | NUMERIC | Requested loan amount |
| `term_months` | INTEGER | Loan term |
| `monthly_income` | NUMERIC | User income |
| `monthly_expenses` | NUMERIC | User expenses |
| `existing_liabilities` | NUMERIC | Existing monthly debt payments |
| `interest_rate` | NUMERIC | Interest rate used for calculation |
| `monthly_payment` | NUMERIC | Calculated monthly payment |
| `debt_to_income_ratio` | NUMERIC | Calculated DTI ratio |
| `risk_level` | VARCHAR | `LOW`, `MEDIUM`, `HIGH` |
| `status` | VARCHAR | `PENDING`, `APPROVED`, `REJECTED` |
| `purpose` | VARCHAR | Loan purpose |
| `created_at` | TIMESTAMP | Created time |
| `updated_at` | TIMESTAMP | Updated time |

---

### `refresh_tokens`

Stores hashed refresh tokens.

This table is only needed if refresh tokens are implemented.

| Column | Type | Notes |
|---|---|---|
| `id` | UUID | Primary key |
| `user_id` | UUID | Foreign key to `app_users` |
| `token_hash` | VARCHAR | Hash of refresh token |
| `expires_at` | TIMESTAMP | Expiration time |
| `revoked_at` | TIMESTAMP | Null if token is active |
| `created_at` | TIMESTAMP | Created time |

---

## Suggested API Endpoints

## Auth

### Register

```http
POST /api/auth/register
```

Request:

```json
{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "Markus",
  "lastName": "Jan"
}
```

---

### Login

```http
POST /api/auth/login
```

Request:

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

Response:

```json
{
  "accessToken": "jwt-access-token",
  "refreshToken": "refresh-token",
  "role": "USER"
}
```

---

### Refresh Token

```http
POST /api/auth/refresh
```

Request:

```json
{
  "refreshToken": "refresh-token"
}
```

---

### Logout

```http
POST /api/auth/logout
```

Request:

```json
{
  "refreshToken": "refresh-token"
}
```

---

## User Loan Applications

### Create Application

```http
POST /api/applications
```

---

### Get My Applications

```http
GET /api/applications
```

---

### Get My Application By ID

```http
GET /api/applications/{id}
```

---

## Admin

### Get All Applications

```http
GET /api/admin/applications
```

---

### Get Application By ID

```http
GET /api/admin/applications/{id}
```

---

### Update Application Status

```http
PATCH /api/admin/applications/{id}/status
```

Request:

```json
{
  "status": "APPROVED"
}
```

---

## Suggested Project Structure

### Backend

```text
backend/
  src/main/java/com/example/loanapp/
    auth/
      AuthController.java
      AuthService.java
      JwtService.java
      RefreshTokenService.java
      dto/
    user/
      User.java
      UserRepository.java
      UserRole.java
    loan/
      LoanApplication.java
      LoanApplicationRepository.java
      LoanApplicationService.java
      LoanApplicationController.java
      RiskLevel.java
      ApplicationStatus.java
      dto/
    admin/
      AdminLoanApplicationController.java
    security/
      SecurityConfig.java
      JwtAuthenticationFilter.java
    common/
      exception/
      config/
  src/main/resources/
    db/migration/
      V1__create_initial_tables.sql
```

### Frontend

```text
frontend/
  src/
    api/
      authApi.ts
      loanApplicationApi.ts
    components/
    router/
      index.ts
    stores/
      authStore.ts
    views/
      LoginView.vue
      RegisterView.vue
      DashboardView.vue
      NewApplicationView.vue
      ApplicationDetailsView.vue
      AdminApplicationsView.vue
```

---

## One-Month Development Plan

## Week 1: Project Setup and Authentication

Goals:

- Create GitHub repository
- Set up Spring Boot backend
- Set up Vue frontend
- Set up PostgreSQL with Docker Compose
- Create `app_users` table
- Implement registration
- Implement login
- Add JWT authentication
- Add protected routes in frontend

Deliverable:

- A user can register and log in
- The frontend can communicate with the backend
- User data is stored in PostgreSQL

---

## Week 2: Loan Application Flow

Goals:

- Create `loan_applications` table
- Implement loan application entity, repository, service, and controller
- Implement monthly payment calculation
- Implement debt-to-income calculation
- Implement risk level calculation
- Create frontend loan application form
- Create user dashboard

Deliverable:

- A user can submit a loan application
- The backend calculates loan results
- The user can view their own applications

---

## Week 3: Admin Functionality and Validation

Goals:

- Add admin role
- Create admin user
- Implement admin endpoints
- Implement status update functionality
- Protect admin endpoints
- Create admin frontend views
- Improve validation and error handling

Deliverable:

- Admin can view all applications
- Admin can approve or reject applications
- Regular users cannot access admin functionality

---

## Week 4: Polish, Testing, and Documentation

Goals:

- Add backend unit tests
- Add endpoint tests
- Add Flyway migrations
- Add Swagger/OpenAPI if desired
- Add GitHub Actions
- Finish Docker Compose setup
- Improve UI
- Write final README
- Add screenshots or demo GIF

Deliverable:

- The project is ready to be shown in a portfolio or internship application

---

## MVP Scope

The MVP should include:

- User registration
- User login
- JWT authentication
- USER and ADMIN roles
- Loan application creation
- Monthly payment calculation
- Debt-to-income ratio calculation
- Risk level calculation
- User dashboard
- Admin dashboard
- Status update by admin
- PostgreSQL persistence
- Flyway migrations
- Docker Compose setup
- Clear README documentation

---

## Optional Bonus Features

Optional features that can be added after the MVP:

- Refresh token rotation
- Email notification simulation
- RabbitMQ event when a loan application is created
- MongoDB audit logs
- Status history table
- Swagger/OpenAPI documentation
- Kubernetes manifests
- Frontend tests
- More advanced filtering and pagination

---

## Security Notes

The backend must enforce all security rules.

Important rules:

- Passwords must be hashed
- Access tokens should be short-lived
- Refresh tokens should be stored hashed
- Users can only view their own applications
- Admin endpoints must require the `ADMIN` role
- Frontend route protection is not enough by itself
- Backend must validate every protected request

---

## What This Project Demonstrates

This project demonstrates the ability to:

- Build a full-stack web application
- Create a REST API with Spring Boot
- Use PostgreSQL for relational data
- Implement authentication and authorization
- Design basic business logic
- Work with role-based access control
- Build a Vue frontend
- Connect frontend and backend cleanly
- Use database migrations
- Structure a project professionally
- Explain a banking-related workflow

---

## Interview Explanation

A short way to explain this project:

> I built a Loan Application Simulator where users can register, log in, and submit simplified loan applications. The backend calculates estimated monthly payment, debt-to-income ratio, and risk level. Applications are stored in PostgreSQL, and admin users can review applications and update their status. The goal was to practice a realistic full-stack workflow using Vue, Java Spring Boot, PostgreSQL, JWT authentication, and role-based access control.

---

## Final Notes

The project should stay focused and realistic. The main priority is to build a working, clean, and understandable MVP rather than adding too many technologies too early.

Recommended first version:

- Vue frontend
- Spring Boot backend
- PostgreSQL database
- JWT auth
- User/admin roles
- Loan application flow

After the MVP works, optional technologies like RabbitMQ, MongoDB, and Kubernetes can be added as portfolio improvements.
