# 🧠 Mindshub V2

> A modern gamified learning platform built with Software Engineering best practices.

Welcome to **Mindshub V2**! 🚀

Mindshub is a web platform designed to improve student engagement through gamification. Teachers can create courses, lessons, and assessments, while students complete activities, earn points based on their academic performance, and redeem rewards in an integrated marketplace.

This repository contains the complete redesign of the original academic project, focusing on modern backend architecture, maintainability, scalability, and Software Engineering best practices.

---

## 🎯 The Problem

Keeping students engaged throughout the learning process remains one of the biggest challenges in education.

Traditional Learning Management Systems (LMS) usually provide educational content and assessments but often lack mechanisms that encourage continuous participation and reward consistent academic performance.

Mindshub was created to address this challenge by combining structured learning with gamification. Students are rewarded for their progress, making the educational experience more engaging while encouraging continuous learning.

---

## ✨ Version 2 Goals

Version 2 is more than a migration from Laravel to Spring Boot.

It is a complete redesign focused on applying modern Software Engineering concepts, improving the domain model, and building a production-ready backend.

The main objectives are:

* 🏗️ Redesign the application architecture.
* 🧩 Apply Clean Architecture principles.
* 📦 Organize the project using **Clean Architecture**.
* 🎯 Improve the domain model.
* 📐 Apply SOLID principles.
* 🧠 Introduce Domain-Driven Design (DDD) concepts where appropriate.
* 🧪 Increase code quality with automated tests.
* 📖 Document architectural decisions using ADRs.
* 📊 Create UML diagrams to support the documentation.
* 🐳 Improve the development environment with Docker.
* 📚 Produce comprehensive technical documentation.
* 🚀 Build a scalable, maintainable, and production-ready application.

---

## 🛠️ Tech Stack

### Backend

* ☕ Java 21
* 🌱 Spring Boot
* 🔒 Spring Security
* 🗄️ Spring Data JPA
* 📜 Flyway

### Frontend

* 🅰️ Angular 22

### Database

* 🐘 PostgreSQL

### DevOps

* 🐳 Docker
* 📈 Grafana

### Version Control

* 🐙 Git
* GitHub

---

## Tests and coverage

Run the complete test suite and generate the JaCoCo coverage report:

```bash
./mvnw clean verify
```

Java 21 and Docker are required. The context integration test starts PostgreSQL
with Testcontainers. Spring tests activate the `test` profile and load
`src/test/resources/application-test.yaml`. Export `JWT_SECRET` (at least 32 bytes)
and `JWT_EXPIRATION` (milliseconds) before running Maven, or configure them in the
IDE test runner. Use a fictitious signing key for tests. The test runner does not
load `.env` through the application `main()` method. SMTP points to localhost
without credentials.

Open `target/site/jacoco/index.html` in a browser to explore coverage by package,
class, method, line and branch. XML and CSV reports are generated in the same
directory. The report includes all production classes, with no coverage exclusions
or minimum threshold configured.

To run only identity tests and generate their coverage (Docker is also required
for the persistence adapter tests):

```bash
./mvnw clean test jacoco:report -Dtest='br.com.mindshub.identity.**'
```

This narrower run measures only the coverage exercised by identity tests.

---

## 📚 Documentation

Project documentation can be found inside the `docs` directory.

It includes:

* 📖 Refactoring Plan
* 🏛️ Architectural Decision Records (ADRs)
* 📐 UML Diagrams
* 🧠 Domain Modeling
* 📋 Functional Requirements
* 📑 Non-Functional Requirements
* 🔍 Architecture Documentation

---

## 🗺️ Roadmap

### Planning

* [x] Create the repository
* [x] Reverse engineer the original project
* [ ] Define the domain model
* [ ] Gather functional requirements
* [ ] Gather non-functional requirements
* [ ] Write ADRs
* [ ] Create UML diagrams

### Backend

* [ ] Implement authentication and authorization
* [ ] Implement course management
* [ ] Implement lesson management
* [ ] Implement assessment management
* [ ] Implement the gamification system
* [ ] Implement the rewards marketplace
* [ ] Add automated tests

### Frontend

* [ ] Develop the Angular application
* [ ] Integrate with the backend API

### DevOps

* [ ] Configure Docker
* [ ] Configure monitoring with Grafana
* [ ] Configure CI/CD pipeline

### Deployment

* [ ] Deploy the application

---

## 📄 License

This project is licensed under the MIT License.
