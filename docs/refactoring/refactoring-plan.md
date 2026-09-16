# Refactoring Plan

## Overview

Mindshub V2 is a complete redesign of the original academic platform.

The original project was developed as a university project using Laravel's MVC architecture. While it successfully delivered the required features, the codebase grew organically, making it difficult to evolve, maintain, and scale.

Version 2 aims to redesign the application from the ground up by applying Software Engineering principles, modern architecture patterns, and domain-driven thinking.

---

# Objectives

The primary goals of this refactoring are:

- Migrate the backend from PHP (Laravel) to Java (Spring Boot).
- Redesign the application architecture.
- Improve maintainability and scalability.
- Rebuild the domain model.
- Apply Clean Architecture.
- Adopt Package by Feature.
- Improve testability.
- Increase documentation quality.
- Build a production-ready application.

---

# Current Architecture (V1)

The original project uses:

- Laravel MVC
- PHP
- MySQL
- Docker
- Nginx

Characteristics:

- MVC architecture.
- Database-first design.
- Limited separation of business rules.
- Domain mixed with framework concerns.
- Organic project growth during development.

---

# Target Architecture (V2)

The new version will adopt:

- Java 21
- Spring Boot
- Clean Architecture
- Package by Feature
- PostgreSQL
- Docker
- JWT Authentication
- REST API

Architecture goals:

- Framework-independent domain.
- Clear separation of responsibilities.
- High cohesion.
- Low coupling.
- Testable business rules.

---

# Domain Refactoring

Instead of migrating database tables directly, the domain will be redesigned.

The application will be organized into business modules:

- Identity
- Academic
- Assessment
- Gamification
- Marketplace
- Social
- Notifications

Each module will evolve independently while communicating through well-defined interfaces.

---

# Engineering Practices

The project will follow:

- SOLID Principles
- Clean Code
- Domain-Driven Design concepts
- Design Patterns
- ADRs
- UML Documentation
- Git Flow
- Conventional Commits

---

# Documentation Strategy

The project documentation is organized as follows:

- README
- Refactoring Plan
- Domain Documentation
- Requirements
- ADRs
- UML Diagrams
- Architecture Documentation

---

# Migration Strategy

The migration will not be incremental.

Instead, the original application will serve as a functional reference while the new version is rebuilt from scratch.

The migration process consists of:

1. Reverse engineering the original system.
2. Redesigning the domain.
3. Defining architectural decisions.
4. Implementing the new backend.
5. Building the new frontend.
6. Testing.
7. Deployment.

---

# Success Criteria

The refactoring will be considered successful when:

- The original features are available in the new version.
- The architecture is easier to evolve.
- The domain model is clearer.
- Business rules are isolated.
- The application is fully documented.
- Automated tests are implemented.