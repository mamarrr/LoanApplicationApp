# Backend API

## Configuration

Set these environment variables outside source control for non-local environments:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET` with at least 32 bytes
- `JWT_ACCESS_TOKEN_TTL_SECONDS`

For local development:

```powershell
docker compose up -d postgres
$env:DB_URL="jdbc:postgresql://localhost:5432/loan_application_app"
$env:DB_USERNAME="loan_app"
$env:DB_PASSWORD="loan_app"
$env:JWT_SECRET="replace-with-a-local-development-secret-32-bytes-min"
.\gradlew.bat bootRun
```

## Admin Bootstrap

Registration intentionally creates regular `USER` accounts only. For local development, register an admin account normally, then promote it in PostgreSQL:

```sql
UPDATE app_users
SET role = 'ADMIN', updated_at = now()
WHERE email = 'admin@example.com';
```

Do not hardcode production admin credentials in source control.

## Endpoints

### Authentication

- `POST /api/auth/register`
- `POST /api/auth/login`

Both return a bearer access token, user profile fields, role, and token lifetime.

Register request:

```json
{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "Regular",
  "lastName": "User"
}
```

Login request:

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

### User Loan Applications

Authenticated users can call:

- `POST /api/applications`
- `GET /api/applications`
- `GET /api/applications/{id}`

The request for creating an application accepts `amount`, `termMonths`, `monthlyIncome`, `monthlyExpenses`, `existingLiabilities`, and optional `purpose`. Calculated fields are produced by the backend.

Create request:

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

### Admin Review

Admins can call:

- `GET /api/admin/applications`
- `GET /api/admin/applications?status=PENDING`
- `GET /api/admin/applications/{id}`
- `PATCH /api/admin/applications/{id}/status`

Status values are `PENDING`, `APPROVED`, and `REJECTED`.

Status update request:

```json
{
  "status": "APPROVED"
}
```

## Error Shape

Validation, authentication, authorization, and not-found failures use:

```json
{
  "timestamp": "2026-06-16T00:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/applications",
  "fieldErrors": {
    "amount": "must be greater than or equal to 1.00"
  }
}
```

## Notes

Risk level is a simplified simulator result. It is not a real credit decision.
