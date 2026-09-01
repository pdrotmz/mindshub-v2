# ADR-008: Centralize Exception Handling

**Date:** 2026-08-06
**Status:** Accepted  
**Author:** Pedro Tomáz

---

## Context

Without a standardized error handling strategy, clients may receive inconsistent responses, making debugging and maintenance more difficult.

---

## Decision

Mindshub V2 will centralize exception handling using Spring's @RestControllerAdvice.

Business, validation, authentication, and unexpected exceptions will be converted into standardized HTTP responses.

---

## Rationale

Centralized exception handling promotes consistency across the API while reducing duplicated error handling logic inside controllers.

It also improves the client experience by returning predictable error responses.

---

## Consequences

### Positive

- Consistent API error responses.
- Reduced duplicated code.
- Easier maintenance.
- Improved debugging.

### Negative

- Requires defining custom exception hierarchy.
- Additional effort to map exceptions appropriately.