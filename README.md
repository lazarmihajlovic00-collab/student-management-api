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

---

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL / PostgreSQL
- Maven

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

- JWT Authentication & Authorization  
- Role-based access control (Admin/User)  
- Advanced filtering and search  
- Unit and integration testing improvements  

---

## How to Run

Clone the repository:

```bash
git clone https://github.com/your-username/student-management-api.git
```

Configure database in `application.properties`

Run the application:

```bash
mvn spring-boot:run
```

---

## Purpose

This project was built as a learning and portfolio project to demonstrate backend development skills using Spring Boot and RESTful architecture principles.
