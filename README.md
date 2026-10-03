# Java Spring Boot Microservices Demo

A hands-on **Java Spring Boot Microservices** project demonstrating how multiple independent services communicate and work together using modern Spring Cloud technologies.

The project is designed as a practical learning project to understand **Microservices Architecture, Service Discovery, Centralized Configuration, API Gateway, OpenFeign, JWT Authentication, Correlation ID, and Load Balancing**.

---

## 🏗️ Architecture

```text
                         ┌──────────────────┐
                         │      Client      │
                         │  Postman / UI    │
                         └────────┬─────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │   API Gateway    │
                         │     :8085        │
                         └────────┬─────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    │                           │
                    ▼                           ▼
           ┌─────────────────┐        ┌─────────────────┐
           │  User Service   │        │  Order Service  │
           │     :8080       │        │     :8082       │
           └────────┬────────┘        └────────┬────────┘
                    │                          │
                    │                          │
                    │                    OpenFeign
                    │                          │
                    └──────────┐      ┌────────┘
                               ▼      ▼
                         ┌──────────────────┐
                         │  Eureka Server   │
                         │      :8761       │
                         └──────────────────┘

                         ┌──────────────────┐
                         │  Config Server   │
                         │      :8888       │
                         └──────────────────┘
```

---

## 🚀 Technologies Used

| Technology            | Purpose                          |
| --------------------- | -------------------------------- |
| Java 21               | Programming Language             |
| Spring Boot 3.5.7     | Application Framework            |
| Spring Cloud 2025.0.0 | Microservices Infrastructure     |
| Spring Cloud Config   | Centralized Configuration        |
| Netflix Eureka        | Service Discovery                |
| Spring Cloud Gateway  | API Gateway                      |
| OpenFeign             | Service-to-Service Communication |
| Spring Security       | Security                         |
| JWT                   | Authentication                   |
| JJWT 0.12.6           | JWT Token Generation             |
| Spring Data JPA       | Database Access                  |
| MySQL                 | Database                         |
| Maven                 | Build Tool                       |
| Git / GitHub          | Version Control                  |
| Postman               | API Testing                      |

---

## 📦 Microservices

### 1. Config Server

**Port:** `8888`

Provides centralized configuration for all microservices.

```text
config-server/
```

Responsibilities:

* Centralized configuration
* External configuration management
* Configuration through Git repository
* Provides configuration to registered services

---

### 2. Eureka Server

**Port:** `8761`

Acts as the service registry.

```text
eureka-server/
```

Responsibilities:

* Service registration
* Service discovery
* Tracks available service instances
* Supports client-side load balancing

Eureka Dashboard:

```text
http://localhost:8761
```

---

### 3. API Gateway

**Port:** `8085`

Acts as the single entry point for client requests.

```text
api-gateway/
```

Responsibilities:

* Request routing
* JWT authentication
* Correlation ID generation
* Request logging
* Gateway filters
* Load-balanced routing

Example routes:

```text
/users/**  → user-service
/orders/** → order-service
```

---

### 4. User Service

**Port:** `8080`

```text
user-service/
```

Responsibilities:

* User registration
* User login
* User persistence
* Password encryption using BCrypt
* JWT generation
* User validation

Main endpoints:

```text
POST /users/register
POST /users/login
```

---

### 5. Order Service

**Port:** `8082`

```text
order-service/
```

Responsibilities:

* Order APIs
* User information lookup
* Communication with User Service
* Exception handling
* Correlation ID logging

The service communicates with User Service using **OpenFeign**.

---

## 🔄 Service Communication

The project demonstrates synchronous communication between microservices using OpenFeign.

```text
Order Service
      │
      │ OpenFeign
      ▼
User Service
```

Example:

```text
Order Service
     ↓
UserClient
     ↓
Eureka
     ↓
User Service
```

The Order Service does not need to know the physical host or port of the User Service.

---

## 🌐 API Gateway Routing

Client requests are sent through the API Gateway.

### User Service

```text
http://localhost:8085/users/**
```

Routes to:

```text
lb://user-service
```

### Order Service

```text
http://localhost:8085/orders/**
```

Routes to:

```text
lb://order-service
```

---

## 🔐 Authentication

JWT-based authentication is implemented at the API Gateway.

Authentication flow:

```text
Client
   ↓
POST /users/login
   ↓
User Service
   ↓
JWT Token
   ↓
Client
   ↓
Authorization: Bearer <token>
   ↓
API Gateway
   ↓
JWT Validation
   ↓
Protected Service
```

Example header:

```http
Authorization: Bearer <JWT_TOKEN>
```

Public endpoints include:

```text
POST /users/register
POST /users/login
GET  /actuator/health
```

Protected endpoints require a valid JWT token.

---

## 🔑 Password Security

Passwords are not stored as plain text.

The application uses:

```text
BCryptPasswordEncoder
```

Password flow:

```text
Plain Password
      ↓
BCrypt
      ↓
Hashed Password
      ↓
MySQL
```

---

## 🔗 Correlation ID

The project implements request correlation using:

```text
X-Correlation-Id
```

The API Gateway generates a correlation ID when required and forwards it to downstream services.

Example:

```text
Client
  │
  │ X-Correlation-Id
  ▼
API Gateway
  │
  ├──────────────► User Service
  │
  └──────────────► Order Service
```

This helps trace a single request across multiple microservices.

---

## ⚖️ Load Balancing

Multiple instances of Order Service can run simultaneously.

Example:

```text
Order Service Instance 1
localhost:8082

Order Service Instance 2
localhost:8083
```

Gateway uses:

```text
lb://order-service
```

Eureka provides service discovery and Spring Cloud LoadBalancer distributes requests between available instances.

---

## 🛡️ Exception Handling

The services include centralized exception handling using Spring's exception-handling mechanisms.

Examples include:

```text
Invalid credentials
User not found
Order not found
Validation errors
Downstream service errors
```

The APIs return structured error responses instead of exposing internal exceptions.

---

## ✅ Request Validation

The project uses Jakarta Bean Validation for request validation.

Example:

```java
@NotBlank(message = "Username is required")
private String username;
```

Invalid requests are rejected before business processing.

---

## 📁 Project Structure

```text
microservices-demo/
│
├── api-gateway/
│
├── config-server/
│
├── eureka-server/
│
├── user-service/
│
├── order-service/
│
├── .gitignore
│
└── README.md
```

> The `kafka-order-service` practice module is intentionally excluded from this GitHub repository.

---

## ⚙️ Configuration

The microservices use Spring Cloud Config Server for centralized configuration.

Config Server:

```text
http://localhost:8888
```

The external configuration repository contains service-specific configuration such as:

```text
user-service.properties
order-service.properties
api-gateway.properties
eureka-server.properties
```

Sensitive values such as database passwords and JWT secrets should not be committed to GitHub.

---

## ▶️ How to Run

Start the services in the following order.

### Step 1 — Config Server

```text
config-server
Port: 8888
```

### Step 2 — Eureka Server

```text
eureka-server
Port: 8761
```

### Step 3 — User Service

```text
user-service
Port: 8080
```

### Step 4 — Order Service

```text
order-service
Port: 8082
```

Optional second instance:

```text
order-service
Port: 8083
```

### Step 5 — API Gateway

```text
api-gateway
Port: 8085
```

---

## 🧪 Testing

The APIs can be tested using Postman.

### Register User

```http
POST http://localhost:8085/users/register
```

Request:

```json
{
  "username": "sanket",
  "password": "password123"
}
```

---

### Login

```http
POST http://localhost:8085/users/login
```

Request:

```json
{
  "username": "sanket",
  "password": "password123"
}
```

The response contains a JWT token.

---

### Access Protected API

Use the returned token:

```http
Authorization: Bearer <JWT_TOKEN>
```

Then call:

```http
GET http://localhost:8085/orders
```

---

## 🔍 Important URLs

| Service                  | URL                     |
| ------------------------ | ----------------------- |
| Eureka Dashboard         | `http://localhost:8761` |
| Config Server            | `http://localhost:8888` |
| API Gateway              | `http://localhost:8085` |
| User Service             | `http://localhost:8080` |
| Order Service            | `http://localhost:8082` |
| Order Service Instance 2 | `http://localhost:8083` |

---

## 📚 Concepts Demonstrated

This project covers the following Microservices concepts:

* Microservices Architecture
* Spring Boot
* Spring Cloud
* Centralized Configuration
* Service Discovery
* Eureka
* API Gateway
* Gateway Filters
* Load Balancing
* OpenFeign
* Inter-service Communication
* JWT Authentication
* Spring Security
* BCrypt Password Encryption
* Request Validation
* Global Exception Handling
* Correlation ID
* Distributed Request Logging
* Multiple Service Instances
* Git & GitHub

---

## 🛣️ Future Enhancements

The project is being developed incrementally.

Planned enhancements include:

* [ ] Role-Based Authorization
* [ ] Resilience4j Circuit Breaker
* [ ] Retry and Timeout Handling
* [ ] Kafka Event-Driven Communication
* [ ] Distributed Tracing
* [ ] Docker
* [ ] Docker Compose
* [ ] CI/CD with GitHub Actions
* [ ] API Documentation with Swagger/OpenAPI
* [ ] Monitoring and Observability
* [ ] Production-level security improvements

---

## 📈 Development Approach

The project follows an incremental development approach where each major feature is maintained as a separate Git commit.

Example:

```text
feat: add user service foundation
feat: add order service
feat: add API gateway routing
feat: add correlation ID tracing
```

This makes the project history easier to understand and demonstrates feature-based development.

---

## 👨‍💻 Author

**Sanket**

Java Full Stack Developer

Skills:

```text
Java
Spring Boot
Spring Cloud
Microservices
Spring Security
JWT
REST APIs
React.js
MySQL
Kafka
Git
GitHub
```

---

## ⭐ Project Purpose

This project is built for practical learning and demonstration of **enterprise-style Java Microservices architecture** using the Spring Boot and Spring Cloud ecosystem.

If you find the project useful, consider giving it a ⭐ on GitHub.
