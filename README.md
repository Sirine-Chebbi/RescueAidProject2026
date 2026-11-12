
# 🚨 RescueAid
### Humanitarian Emergency Management Platform

<p align="center">
  <strong>Connecting people. Coordinating resources. Saving lives.</strong>
</p>

<p align="center">
  A microservices-based platform designed to coordinate humanitarian
  assistance during natural disasters and emergency situations.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Architecture-Microservices-blueviolet?style=for-the-badge" alt="Microservices"/>
  <img src="https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker"/>
</p>

---

## 📌 About the Project

**RescueAid** is a robust and scalable humanitarian emergency management platform built using **Java Spring Boot and Microservices Architecture**.

Its main objective is to centralize and optimize humanitarian operations during natural disasters such as:

- 🌍 Earthquakes
- 🌊 Floods
- 🔥 Natural disasters
- 🚨 Emergency situations

The platform connects different stakeholders through a centralized system:

| 👤 Stakeholder | 🎯 Main Role |
|---|---|
| Citizens | Report incidents and express emergency needs |
| Donors | Support humanitarian campaigns |
| Associations | Coordinate humanitarian interventions |
| Administrators | Supervise and manage platform operations |

---

## 🏗️ System Architecture

RescueAid follows a **Microservices Architecture** based on the principle:

> Database per Service — Each microservice owns and manages its own database.

The system consists of **7 independent microservices**, each responsible for a specific business domain, and a centralized **API Gateway** that handles client requests.

### 📊 Microservices Overview

| Microservice | Port | Main Responsibilities | Team Member |
|---|:---:|---|---|
| **MS1 – User Service** | `8081` | User accounts, authentication, JWT and role management | Entire Team |
| **MS2 – Incident Service** | `8082` | Incident reporting, victim needs assessment and priority scoring | Chebbi Sirine |
| **MS3 – Location Service** | `8083` | Geographic information, risk zones and intervention locations | Taouai Maryem |
| **MS4 – Donation Service** | `8084` | Donation campaigns, financial contributions and material donations | Zarai Eya |
| **MS5 – Resource Service** | `8085` | Warehouse inventory, shortage alerts and resource allocation | Dridi Tassnim |
| **MS6 – Association Service** | `8086` | NGO registry, specializations, availability and team recommendations | Younes Mezen |
| **MS7 – Transport Service** | `8087` | Vehicle fleet, drivers, evacuation and delivery route planning | Meddeb Moemen |
| **API Gateway** | `8080` | Central entry point, request routing and JWT validation | Entire Team |

### 🔐 Security & Authentication

The platform uses **Spring Security and JSON Web Tokens (JWT)** to secure communication and manage access permissions.

Supported roles:

- `CITIZEN`
- `DONOR`
- `ASSOCIATION`
- `ADMIN`

---

## 📂 Project Structure

```text
rescueAid-4das-gX/
│
├── Backend/
│   ├── MS1-User/
│   ├── MS2-IncidentNeeds/
│   ├── MS3-LocationZone/
│   ├── MS4-DonationCampaign/
│   ├── MS5-ResourceDistribution/
│   ├── MS6-Association/
│   └── MS7-Transport/
│
├── Frontend/
│   └── rescueaid-web-app/
│
├── Documentation/
│   ├── Presentation/
│   │
│   └── Diagrams/
│
├── RescueAid.pdf
└── README.md
```

---

## 🛠️ Technology Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security

### Microservices Communication
- Spring Cloud Gateway
- OpenFeign
- Resilience4j

### Database
- MySQL 8.0
- MySQL Spatial (Location Service)
- Database per Service architecture

### Security
- JSON Web Token (JWT)
- Stateless Authentication
- Role-Based Access Control (RBAC)

### DevOps & Tools
- Docker
- Docker Compose
- Maven
- Git & GitHub
- Git Flow
- GitHub Projects

---

## 🚀 Getting Started

Follow these steps to run the RescueAid backend locally.

### 1. Prerequisites

Make sure you have installed:

| Tool | Required Version |
|---|---|
| Java JDK | 17+ |
| Maven | 3.8+ |
| MySQL | 8.0 |
| Docker | Latest stable |
| Docker Compose | V2 |

### 2. Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd rescueAid-4das-gX
```

### 3. Start the Databases

Using Docker Compose:

```bash
docker compose up -d mysql-databases
```

### 4. Run a Microservice

For example, to start the User Service:

```bash
cd Backend/ms1-user-service
mvn spring-boot:run
```

### 5. Start the API Gateway

```bash
cd Backend/api-gateway
mvn spring-boot:run
```

The API Gateway will be accessible at:

```text
http://localhost:8080
```

> **Note:** Configure the database credentials and environment variables before starting the services. The Docker Compose service name and configuration must match your actual `docker-compose.yml`.

---

## 👥 Development Team

<p align="center">
  <strong>RescueAid – 4DAS Engineering Project</strong>
</p>

| Team Member | Responsibility |
|---|---|
| **Chebbi Sirine** | MS2 – Incident & Needs Management |
| **Taouai Maryem** | MS3 – Location & Risk Zones |
| **Zarai Eya** | MS4 – Donations & Campaigns |
| **Dridi Tassnim** | MS5 – Resources & Distribution |
| **Younes Mezen** | MS6 – Associations Management |
| **Meddeb Moemen** | MS7 – Transport & Logistics |

**Shared Development:** MS1 – User Service & API Gateway.

---

## 📚 Documentation

Additional project documentation is available in the `Documentation/` directory:

- 📊 Global System Architecture
- 📐 Global Class Diagram
- 📑 Technical Project Report
- 🎤 Week 4 Presentation

---

## 📜 License & Academic Context

This project is developed as part of the **4DAS Engineering Program**.

**Academic Milestone:** Week 4 Validation.

---

<p align="center">
  <strong>🚨 RescueAid</strong><br/>
  <i>Technology serving humanity.</i>
</p>
