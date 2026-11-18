# 🎫 High-Security Dynamic QR Ticket Engine

> A high-performance, resilient event ticket verification engine featuring time-rotating Dynamic QR codes (TOTP HMAC-SHA256), atomic anti-double check-in, and clock drift tolerance.

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20Layered-blue.svg)](#architecture)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📌 Problem Solved

Traditional event ticketing systems using static barcodes or QR codes suffer from critical vulnerabilities:
1. **Screenshot Resale Fraud**: A scalper purchases 1 legitimate ticket, takes a screenshot, and resells it to multiple buyers online.
2. **Replay & Concurrency Attacks**: The same ticket code scanned at two turnstiles simultaneously causes double admission.

### The Solution
* **Dynamic QR Rotation (30s TOTP)**: Each ticket generates a 6-digit cryptographic token using `HMAC-SHA256` that rotates every 30 seconds. Screenshots become invalid after 30 seconds.
* **Clock Drift Tolerance**: Automatically tolerates time discrepancies of $\pm 30$ seconds between client devices and the server.
* **Atomic State Transition**: Concurrency-safe check-in using database conditional updates, guaranteeing that 1 ticket can never pass 2 gates at the same millisecond.

---

## 🏗️ Architecture & Tech Stack

* **Language**: Java 21 LTS
* **Framework**: Spring Boot 4.1.2 (Spring Web, Spring Data JPA, Validation)
* **Database**: PostgreSQL 16
* **Documentation & Testing**: Springdoc OpenAPI / Swagger UI
* **Design Pattern**: Standard 3-Tier Layered Architecture (Controller $\rightarrow$ Service $\rightarrow$ Repository)

---

## 🚀 Quick Start

### Prerequisites
* Java 21 LTS
* Docker & Docker Compose (for PostgreSQL)

### 1. Start Database
```bash
docker compose up -d
```

### 2. Run Backend API
```bash
cd apps/backend-api
./mvnw spring-boot:run
```

API will be live at `http://localhost:8080/api/v1`.

### 3. API Documentation & Interactive Testing
Once the application is running, open your browser:
* **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
* **OpenAPI Docs**: `http://localhost:8080/v3/api-docs`

---

## 📡 Core API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/users/{userId}/tickets` | Get all tickets owned by a specific user |
| `GET` | `/api/v1/tickets/{ticketId}/qr?userId={userId}` | Generate 30s rotating dynamic QR payload |
| `POST` | `/api/v1/gate/check-in` | Gate turnstile scan & atomic check-in |
