# ADR-009: Adopt Role-Based Access Control (RBAC)

**Date:** 2026-08-06
**Status:** Accepted  
**Author:** Pedro Tomáz

---

## Context

Mindshub supports different types of users, each with distinct responsibilities and permissions.

Access to application resources must be controlled according to each user's role.

---

## Decision

Mindshub V2 will implement Role-Based Access Control (RBAC).

Initially, the system will support the following roles:

- Administrator
- Teacher
- Student

Permissions will be assigned based on roles rather than individual users.

---

## Rationale

RBAC is simple to implement, easy to understand, and suitable for educational platforms where responsibilities are naturally grouped into predefined roles.

It also integrates well with Spring Security.

---

## Consequences

### Positive

- Clear permission model.
- Easier authorization management.
- Good integration with Spring Security.
- Scalable for future roles.

### Negative

- Less flexible than attribute-based authorization.
- May require additional roles as the application evolves.