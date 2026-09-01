# ADR-0002 — Organize Code Using Package by Feature

**Date:** 2026-08-05
**Status:** Accepted  
**Author:** Pedro Tomáz

---

## Context

The first version of Mindshub organized the source code by technical layers, making it more difficult to navigate as the project evolved.

For Version 2, the project requires a structure that groups related components together, improving maintainability and making it easier to locate and develop new features.

---

## Decision

The project will organize the codebase using the **Package by Feature** approach.

Each business feature will contain its own controllers, services, repositories, DTOs, entities, and other related classes.

Example:

```text
entity/
├── controller/
├── service/
├── repository/
├── dto/
└── entity/

assessment/
├── controller/
├── service/
├── repository/
├── dto/
└── entity/

marketplace/
├── controller/
├── service/
├── repository/
├── dto/
└── entity/
```

---

## Rationale

Package by Feature was chosen because it:

- Groups related classes by business capability instead of technical responsibility.
- Improves project navigation.
- Increases cohesion within each feature.
- Reduces coupling between different modules.
- Makes new features easier to develop and maintain.
- Scales better as the project grows.

---

## Consequences

### Positive

- Better project organization.
- Easier navigation through the codebase.
- Improved maintainability.
- Higher cohesion between related classes.
- Simpler onboarding for new developers.

### Negative

- May introduce some duplication between features.
- Requires consistent package organization across the project.