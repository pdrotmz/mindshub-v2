# ADR-010: Use Flyway for Database Versioning

**Date:** 2026-08-06
**Status:** Accepted  
**Author:** Pedro Tomáz

---

## Context

Database schema changes must be tracked, versioned, and reproducible across all development and deployment environments.

Manual database updates increase the risk of inconsistencies and deployment failures.

---

## Decision

Mindshub V2 will use Flyway as the database migration tool.

All schema changes will be managed through versioned SQL migration scripts stored in the project's source code.

---

## Rationale

Flyway provides a reliable and deterministic approach to database versioning.

By treating database changes as part of the application's source code, every environment remains synchronized, reducing deployment risks and improving collaboration among developers.

---

## Consequences

### Positive

- Version-controlled database schema.
- Reproducible deployments.
- Simplified collaboration.
- Easier rollback planning.

### Negative

- Developers must follow migration conventions.
- Incorrect migration scripts may affect deployment if not properly tested.