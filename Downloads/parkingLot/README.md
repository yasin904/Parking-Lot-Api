# Parking Lot API

A scalable and production-ready Lot Management API with Kotlin + SpringBoot.
## 🧰 Tech Stack

- Kotlin + Spring Boot
- jOOQ (SQL Query Builder)
- PostgreSQL (via Docker)
- Flyway (Database Migrations)
- Docker + Docker Compose
- Swagger (OpenAPI) Docs
- JUnit5 (Unit Tests)

---

## 🚀 Features

- Create parking slots by vehicle type
- Park and unpark vehicles
- Get available/occupied slots
- Remove slots (with safety checks)
- Request/response DTOs with validation
- Exception handling using `@ControllerAdvice`
- Swagger UI for API exploration

---

## 🛠️ Setup Instructions

### 1. Clone the repo

```bash
git clone https://github.com/<your-username>/parking-lot-api.git
cd parking-lot-api