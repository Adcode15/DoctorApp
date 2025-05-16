# 🏥 Doctor-Patient Management Platform

A Spring Boot-based REST API that powers a simple doctor-patient management system. It enables doctors to register, patients to sign up, and allows the platform to suggest suitable doctors based on symptoms and location.

## ✨ Key Features

- Register and delete doctors
- Register and delete patients
- Suggest doctors based on patient symptoms and city
- Input validation and clean error handling
- Swagger UI for interactive API documentation
- In-memory H2 database for testing

---

## ⚙️ Tech Stack

- **Backend**: Java 17, Spring Boot, Hibernate
- **Database**: H2 (in-memory)
- **API Testing**: Postman
- **Documentation**: Swagger UI

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+

### Run Locally

```bash
git clone https://github.com/your-username/doctor-patient-platform.git
cd doctor-patient-platform
mvn spring-boot:run
