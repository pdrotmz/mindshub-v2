# ADR-003: Use RESTful API

**Date:** 2026-08-06
**Status:** Accepted  
**Author:** Pedro Tomáz

---

## Context

Mindshub V2 exposes its functionality through a backend API consumed by a web client. A consistent communication protocol is required to ensure interoperability, maintainability, and ease of integration.

---

## Decision

Mindshub V2 will expose its backend functionality through a RESTful API using HTTP and JSON.

The API will follow REST principles, including resource-oriented endpoints, proper HTTP methods, standardized status codes, and stateless communication.

---

## Rationale

REST is a mature and widely adopted architectural style that integrates seamlessly with Spring Boot and modern frontend frameworks such as Angular.

It provides a simple, scalable, and standardized approach for exposing application resources while remaining independent of client implementations.

---

## Consequences

### Positive

- Standardized communication between frontend and backend.
- Simple integration with external clients.
- Stateless requests improve scalability.
- Easy adoption by developers familiar with REST.

### Negative

- Not ideal for highly complex querying scenarios.
- Requires careful endpoint design to avoid inconsistencies.