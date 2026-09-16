# ADR-0005 - Adopt JWT Authentication

**Date:** 2026-08-08  
**Status:** Accepted  
**Author:** Pedro Tomáz

---

## Context

Version 2 aims to improve the authentication mechanism used in the first version of Mindshub.

The project requires a secure, stateless authentication mechanism that integrates well with Spring Security and supports future scalability.

---

## Decision

The project will adopt **JSON Web Token (JWT)** as its authentication mechanism.

---

## Rationale

JWT was chosen because it:

- Provides stateless authentication, eliminating the need for server-side session storage.
- Integrates seamlessly with Spring Security.
- Improves scalability since authentication information is stored in the token itself.
- Is widely adopted in RESTful APIs.
- Enables authentication across multiple services without relying on shared sessions.
- Reduces server memory usage compared to session-based authentication.

---

## Consequences

### Positive

- Stateless authentication.
- Better scalability.
- Easy integration with Spring Security.
- Suitable for REST APIs.
- Simplifies horizontal scaling.

### Negative

- Tokens cannot be revoked easily before expiration.
- Token expiration and refresh strategies must be implemented.
- Sensitive information should never be stored inside the JWT payload.