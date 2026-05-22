# Student Management API

A RESTful backend application built with **Spring Boot** for managing student data.  
The project demonstrates clean layered architecture and standard backend development practices.

---

## Features

- Create, read, update, and delete students (CRUD operations)
- RESTful API design with proper HTTP status handling
- Input validation and centralized error handling
- Pagination and sorting support
- Layered architecture (Controller / Service / Repository)
- Search endpoints
- Swagger / OpenAPI documentation
- Swagger/OpenAPI integration with JWT authorization support
- Dockerized PostgreSQL database
- JWT-based authentication and endpoint protection
- Role-based authorization (USER / ADMIN)

---

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Spring Security
- Docker
- Swagger / OpenAPI

---

## Project Structure

```text
src/main/java/com/student

- controller → REST controllers (API endpoints)
- service → Business logic layer
- repository → Data access layer
- entity → JPA entities
- dto → Data Transfer Objects (if used)
- exception → Custom exceptions and handlers
- config → Configuration classes
```

---

## Authentication

The API uses JWT-based authentication with Spring Security.
JWT tokens can be tested directly through Swagger UI using the Authorize button.

### Public Endpoints

- POST `/auth/register`
- POST `/auth/login`

### Protected Endpoints

All `/api/students/**` endpoints require a valid JWT token.

Example header:

```http
Authorization: Bearer your_jwt_token
```
### Access Rules

- USER role:
    - Can read student data (GET requests)

- ADMIN role:
    - Can read, create, update, and delete students

---

## API Endpoints

### Students

| Method | Endpoint           | Description         |
|--------|--------------------|---------------------|
| GET    | /api/students      | Get all students    |
| GET    | /api/students/{id} | Get student by ID   |
| POST   | /api/students      | Create new student  |
| PUT    | /api/students/{id} | Update student      |
| DELETE | /api/students/{id} | Delete student      |

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

## Features in Progress

- Advanced filtering and search
- Unit and integration testing improvements
- Refresh token support  

---

## How to Run

Clone the repository:

```bash
git clone https://github.com/lazarmihajlovic00-collab/student-management-api.git
```

Install dependencies:

```bash
mvn clean install
```

Configure database in `application.properties`

Run the application:

```bash
mvn spring-boot:run
```

Access Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```
Use the Swagger "Authorize" button to test protected endpoints with JWT tokens.
---

## Purpose

This project was built as a backend learning and portfolio project to demonstrate practical Spring Boot development skills, REST API design, authentication and authorization, and modern backend architecture principles.