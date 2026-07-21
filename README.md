# 🎓 Student Management API

A production-ready, secure RESTful backend application built with **Spring Boot 3** and **Java 21**.  
The project demonstrates clean layered architecture, enterprise security, containerization with Docker, automated CI/CD pipelines, and live cloud deployment.

---

## 🚀 Live Demo & API Documentation

- **Live Application API:** Deployed on Render Cloud
- **Interactive Swagger Documentation:** Access `/swagger-ui/index.html` on the deployed domain to test endpoints with JWT authorization.

---

## ✨ Features

- **Full CRUD Operations:** Comprehensive management of student profiles, departments, courses, and grades.
- **RESTful API Design:** Standardized HTTP status codes, custom DTO mappings, and request validation.
- **Enterprise Security:** Stateless JWT authentication powered by Spring Security, featuring password encryption with BCrypt.
- **Role-Based Access Control (RBAC):** Distinct permission levels (`USER` vs `ADMIN`).
- **Refresh Token Lifecycle:** Secure refresh token rotation, database token persistence, expiration handling, and token revocation/logout flow.
- **Database Migrations:** Automated schema versioning and database evolution using **Flyway**.
- **Data Querying:** Built-in pagination, sorting, and dynamic search capabilities.
- **Containerization & Cloud CI/CD:**
    - Dockerized application and local setup via `docker-compose`.
    - Automated GitHub Actions workflow for building, testing, and pushing images to **Docker Hub**.
    - Continuous deployment connected to Render Cloud with managed PostgreSQL persistence.
- **Automated Testing:** Comprehensive unit and integration test coverage using JUnit 5, Mockito, and H2 in-memory database.

---

## 🛠️ Tech Stack

- **Core & Framework:** Java 21, Spring Boot 3
- **Security:** Spring Security, JSON Web Tokens (JWT)
- **Persistence & DB:** Spring Data JPA, Hibernate, PostgreSQL, Flyway Migrations
- **Containerization & DevOps:** Docker, Docker Hub, Docker Compose
- **CI/CD & Hosting:** GitHub Actions, Render Cloud Services
- **Testing:** JUnit 5, Mockito, H2 Database
- **Documentation & Tools:** Swagger / OpenAPI, Maven

---

## 🏗️ Architecture & Project Structure

The codebase follows a clean, feature-focused layered architecture:

```text
src/main/java/com/student
├── auth/           # Authentication endpoints, JWT generation & filtering logic
├── config/         # Security rules, Swagger OpenAPI & app configuration
├── student/        # Student domain entity, DTOs, Repository, Service & Controller
├── department/     # Department management domain logic
├── course/         # Course domain logic
├── grade/          # Grading domain logic
├── refreshtoken/   # Refresh token lifecycle & DB repository
├── common/         # Base entities & shared audit utilities
└── exception/      # Centralized global exception handler & error response models
```

---

## 🔒 Security & Role Rules

All `/api/**` endpoints require a valid JWT Bearer token passed in the HTTP Authorization header:

```http
Authorization: Bearer <your_jwt_access_token>
```

### Access Matrix

| Role | Allowed Actions |
| :--- | :--- |
| **USER** | Read student records, search, filter, and view course/department details (GET requests). |
| **ADMIN** | Full authority: Create, Update, Delete students, assign courses, manage roles and system resources. |

---

## ⚡ Automated CI/CD Workflow

Every commit pushed to the `main` branch automatically triggers the GitHub Actions pipeline:

1. **Continuous Integration (CI):** Sets up JDK 21 environment, resolves dependencies, and executes unit and integration tests.
2. **Dockerization:** Builds an immutable Docker image using multi-stage builds.
3. **Registry Push:** Authenticates securely with Docker Hub and pushes tagged image releases (`latest` and `<commit-sha>`).
4. **Continuous Deployment (CD):** Signals the cloud infrastructure to pull the updated image and deploy zero-downtime updates connected to the managed PostgreSQL instance.

---

## 💻 Local Development Setup

### Prerequisites

- Docker Desktop installed and running
- Java 21 JDK & Maven (optional if using Docker)

### Running with Docker Compose (Recommended)

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/lazarmihajlovic00-collab/student-management-api.git](https://github.com/lazarmihajlovic00-collab/student-management-api.git)
   cd student-management-api
   ```

2. **Start the application & PostgreSQL database:**
   ```bash
   docker-compose up -d
   ```

3. **Access the local Swagger UI:**
   Open `http://localhost:8080/swagger-ui/index.html` in your browser.