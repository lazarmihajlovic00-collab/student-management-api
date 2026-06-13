````md
# Student Management API

A RESTful backend application built with **Spring Boot** for managing student data.  
The project demonstrates clean layered architecture, secure API development, and modern backend engineering practices.

---

## Features

- Create, read, update, and delete students (CRUD operations)
- RESTful API design with proper HTTP status handling
- DTO-based request handling and input validation
- Centralized exception handling
- Pagination, sorting, and search functionality
- Layered architecture (Controller / Service / Repository)
- JWT-based authentication with Spring Security
- Password encryption using BCrypt
- Role-based authorization (USER / ADMIN)
- Protected endpoints with access control
- Refresh token implementation
- Token revocation/logout functionality
- Swagger / OpenAPI documentation
- Swagger JWT authorization support
- Dockerized PostgreSQL database
- Unit testing with JUnit 5 and Mockito
- H2 in-memory database for testing
- GitHub Actions CI pipeline

---

## Tech Stack

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Docker
- Swagger / OpenAPI
- JUnit 5
- Mockito
- H2 Database
- GitHub Actions

---

## Project Structure

```text
src/main/java/com/student

- controller → REST API endpoints
- service → Business logic layer
- repository → Database access layer
- entity → JPA entities
- dto → Request / response DTOs
- exception → Custom exceptions and global handlers
- auth → Authentication and JWT logic
- token → Refresh token management
- config → Security and application configuration
```

---

## Authentication & Authorization

The application uses JWT-based authentication with Spring Security.

Spring Security is used to enforce role-based access rules for protected endpoints.

### Authentication Flow

1. User registers or logs in
2. Server validates credentials
3. Access token (JWT) is generated
4. Refresh token is generated and stored in the database
5. JWT token is used to access protected endpoints
6. Refresh token can generate a new access token when the old one expires

JWT tokens can be tested directly in Swagger UI using the **Authorize** button.

---

## Public Endpoints

```http
POST /auth/register
POST /auth/login
POST /auth/refresh
POST /auth/logout
```

---

## Protected Endpoints

All `/api/students/**` endpoints require a valid JWT access token.

Example header:

```http
Authorization: Bearer your_jwt_token
```

---

## Access Rules

### USER Role

- Can read student data (GET requests)

### ADMIN Role

- Can create students
- Can update students
- Can delete students

---

## API Endpoints

### Students

| Method | Endpoint           | Description        |
|--------|--------------------|--------------------|
| GET    | /api/students      | Get all students   |
| GET    | /api/students/{id} | Get student by ID  |
| POST   | /api/students      | Create new student |
| PUT    | /api/students/{id} | Update student     |
| DELETE | /api/students/{id} | Delete student     |

---

## Example Request

### Create Student

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "age": 21
}
```

---

## Example Authentication Response

```json
{
  "accessToken": "jwt_access_token",
  "refreshToken": "refresh_token"
}
```

---

## Refresh Token System

The project includes refresh token support for improved authentication security.

### Refresh Token Features

- Refresh tokens stored in PostgreSQL
- Token expiration handling
- Token revocation support
- Secure logout flow
- New JWT generation without re-login

---

## Testing

The project includes automated unit testing focused on the service layer.

### Testing Stack

- JUnit 5
- Mockito
- H2 Database

### Covered Scenarios

- Student creation
- Student retrieval
- Student update
- Student deletion
- Validation of duplicate emails
- Student not found scenarios
- Exception handling paths

Tests are automatically executed through the GitHub Actions CI pipeline on every push and pull request.

## Features in Progress

- Advanced filtering and dynamic search
- Integration testing
- API security hardening
- Clean Architecture refactor
- Continuous Deployment (CD)

---

## How to Run

Clone the repository:

```bash
git clone https://github.com/lazarmihajlovic00-collab/student-management-api.git
```

Move into the project directory:

```bash
cd student-management-api
```

Install dependencies:

```bash
mvn clean install
```

Configure the database inside:

```text
src/main/resources/application.properties
```

Run the application:

```bash
mvn spring-boot:run
```

---

## Swagger UI

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

Use the **Authorize** button to test protected endpoints with JWT tokens.

---

## Purpose

This project was built as a backend engineering and portfolio project to demonstrate:

- REST API development
- Spring Boot backend engineering
- Authentication and authorization
- Secure API design
- Database integration with JPA/Hibernate
- Production-style backend architecture principles
````
