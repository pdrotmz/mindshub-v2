# ADR-007: Adopt a Comprehensive Testing Strategy

**Date:** 2026-08-06
**Status:** Accepted  
**Author:** Pedro Tomáz

---

## Context

Mindshub V2 aims to become a maintainable and production-ready application. Automated testing is essential to prevent regressions and ensure business rules remain reliable as the system evolves.

---

## Decision

The project will adopt multiple testing levels:

- Unit Tests using JUnit 5 and Mockito.
- Integration Tests using Testcontainers.
- Code coverage analysis using JaCoCo.

---

## Rationale

Different testing levels validate different aspects of the application.

Unit tests verify business logic in isolation, while integration tests ensure the application correctly interacts with external dependencies such as PostgreSQL.

This strategy increases confidence during refactoring and continuous development.

---

## Consequences

### Positive

- Increased software reliability.
- Safer refactoring.
- Early detection of regressions.
- Higher code quality.

### Negative

- Longer development time.
- Increased CI execution time.
- Additional maintenance of test suites.