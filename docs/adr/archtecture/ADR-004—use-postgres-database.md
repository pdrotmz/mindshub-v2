# ADR-0002 - Adopt PostgreSQL and Testcontainers

**Date:** 2026-08-05
**Status:** Accepted  
**Author:** Pedro Tomáz

---

## Context

Version 2 aims to improve the project's development workflow and testing strategy by adopting tools commonly used in modern software engineering.

The project requires a reliable relational database for development and production while ensuring that automated tests run in an environment as close as possible to production.

---

## Decision

The project will adopt **PostgreSQL** as the primary database for test, development and production.

Automated integration tests will use **Testcontainers** to provision disposable PostgreSQL containers during test execution.

---

## Rationale

This decision was made because:

- PostgreSQL is a robust, reliable, and widely adopted relational database.
- Using the same database engine in development, testing, and production reduces environment inconsistencies.
- Testcontainers creates isolated databases for each test execution.
- Tests become more reliable because they run against a real PostgreSQL instance.
- The testing environment is automatically provisioned and removed after execution.
- Testcontainers integrates seamlessly with Spring Boot and JUnit.

---

## Consequences

### Positive

- Greater confidence in integration tests.
- Development and production environments remain consistent.
- Reduced risk of database-specific issues.
- Better CI/CD compatibility.
- Improved test isolation.

### Negative

- Test execution is slower than using an in-memory database.
- Docker must be available on the machine running the tests.
- Initial setup is more complex than H2.