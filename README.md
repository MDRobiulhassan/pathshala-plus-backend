# Pathshala Plus Backend

Backend API for Pathshala Plus, an educational platform for browsing academic content, managing tests, and tracking student progress.

The application is built with Spring Boot and uses PostgreSQL for persistence. Database schema changes are managed with Flyway.

## Features

- User signup, login, logout, token refresh, and profile management
- Google OAuth2 authentication
- JWT-based authentication
- Password reset with email OTP verification
- Education levels, institutes, units, subjects, chapters, and exam sessions
- Question bank and question browsing
- Test attempts, saved answers, submissions, results, and history
- Student dashboard data
- User avatar storage through Supabase Storage
- OpenAPI and Swagger UI for development profiles
- Database migrations with Flyway

## Technology Stack

- Java 17
- Spring Boot 4
- Spring Web MVC and WebFlux
- Spring Data JPA and Hibernate
- PostgreSQL 16
- Flyway
- Spring Security
- JWT
- OAuth2 Client
- Supabase Storage
- Maven
- Docker and Docker Compose

## Requirements

Install the following before running the project:

- JDK 17 or newer
- Docker Desktop, if using Docker Compose
- PostgreSQL 16 or another compatible PostgreSQL version, if running the application directly

Verify Java and Maven Wrapper availability:

```powershell
java -version
.\mvnw.cmd -version
```

## Configuration

The application supports these Spring profiles:

| Profile | Purpose |
| --- | --- |
| `local` | Local development with values defined in `application-local.yaml` |
| `dev` | Development configuration using environment variables |
| `prod` | Production configuration using environment variables |

For `dev` and `prod`, configure the following variables before starting the application:

```env
SPRING_PROFILES_ACTIVE=dev

DATABASE_URL=jdbc:postgresql://localhost:5432/pathshala-database
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=change-me

JWT_SECRET=replace-with-a-long-random-secret
JWT_ACCESS_EXPIRATION_MINUTES=30
JWT_REFRESH_EXPIRATION_DAYS=14

GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
GOOGLE_REDIRECT_URI=http://localhost:3000/api/v1/auth/oauth2/callback/google
GOOGLE_AUTHORIZATION_URI=https://accounts.google.com/o/oauth2/auth
GOOGLE_TOKEN_URI=https://oauth2.googleapis.com/token
GOOGLE_USER_INFO_URI=https://www.googleapis.com/oauth2/v3/userinfo
GOOGLE_USER_NAME_ATTRIBUTE=sub

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-mailbox
MAIL_PASSWORD=your-mail-app-password
MAIL_SENDER_NAME=Pathshala Support

FRONTEND_URL=http://localhost:3000
OTP_EXPIRATION_MINUTES=5
RESET_TOKEN_EXPIRATION_MINUTES=15

SUPABASE_URL=https://your-project.supabase.co
SUPABASE_SERVICE_ROLE_KEY=your-service-role-key
SUPABASE_STORAGE_PUBLIC_BASE_URL=https://your-project.supabase.co
```

Do not commit `.env` files, passwords, private keys, OAuth client secrets, JWT secrets, or Supabase service role keys.

## Run with Docker Compose

Docker Compose starts the backend and PostgreSQL services.

1. Create or update `.env` in the project root with the required application values and these database values:

```env
SPRING_PROFILES_ACTIVE=dev
POSTGRES_DB=pathshala-database
POSTGRES_USER=postgres
POSTGRES_PASSWORD=change-me
POSTGRES_PORT=5432
```

2. Build the application JAR:

```powershell
.\mvnw.cmd clean package
```

3. Start the services:

```powershell
docker compose up --build
```

The API is available at `http://localhost:8080`.

To stop the services:

```powershell
docker compose down
```

To stop the services and remove the database volume:

```powershell
docker compose down -v
```

Removing the volume deletes the local PostgreSQL data.

## Run with Maven

Start PostgreSQL locally, create the database, and configure the active profile.

For local development:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

For an environment-variable-based profile:

```powershell
$env:SPRING_PROFILES_ACTIVE = "dev"
.\mvnw.cmd spring-boot:run
```

The application starts on port `8080` unless another port is configured.

## Build and Test

Run the test suite:

```powershell
.\mvnw.cmd test
```

Create a production JAR:

```powershell
.\mvnw.cmd clean package
```

Run the generated JAR:

```powershell
java -jar target/pathshala-plus-backend-0.0.1-SNAPSHOT.jar
```

## Database Migrations

Flyway runs automatically when the application starts. Migration files are located in:

```text
src/main/resources/db/migration
```

Hibernate is configured with `ddl-auto=validate`, so the database schema must match the migration history.

## API Documentation

Swagger UI is enabled for the `local` and `dev` profiles:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON is available at:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI and OpenAPI endpoints are disabled in the `prod` profile.

## Main API Areas

All API routes use the `/api/v1` prefix.

| Area | Base route |
| --- | --- |
| Authentication | `/api/v1/auth` |
| Users | `/api/v1/users` |
| Student dashboard | `/api/v1/dashboard` |
| Education levels | `/api/v1/education-levels` |
| Institutes and units | `/api/v1/institutes` |
| Exam sessions and information | `/api/v1` |
| Languages | `/api/v1/languages` |
| Subjects | `/api/v1/subjects` |
| Questions | `/api/v1/questions` and related exam routes |
| Tests and attempts | `/api/v1/tests` |

Use Swagger UI for request schemas, authentication requirements, and the complete endpoint list.

## Project Structure

```text
src/
+-- main/
|   +-- java/com/example/pathshala_plus_backend/
|   |   +-- controller/       REST controllers
|   |   +-- service/          Service interfaces
|   |   +-- serviceimpl/      Service implementations
|   |   +-- repository/       Spring Data repositories
|   |   +-- entity/           JPA entities
|   |   +-- dto/              Request and response DTOs
|   |   +-- security/         JWT and security configuration
|   |   +-- config/           Application configuration
|   |   +-- exceptions/       Exception types and handlers
|   +-- resources/
|       +-- db/migration/     Flyway migrations
|       +-- application-*.yaml
+-- test/
    +-- java/                 Automated tests
```

## License

No license has been specified for this project.
