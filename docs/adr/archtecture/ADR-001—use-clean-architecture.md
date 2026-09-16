# ADR-001 — Use Clean Architecture

**Date:** 2026-08-05  
**Status:** Accepted  
**Author:** Pedro Tomáz

---

## Context

The first version of Mindshub was developed using the MVC architecture, which was chosen because it is the default architecture provided by Laravel and was already familiar to the development team.

Although MVC was sufficient for the initial academic project, the application evolved to include multiple business domains, such as Academic, Assessment, Gamification, Marketplace, and Social. As the business logic became more complex, maintaining a clear separation between domain rules and framework-specific code became increasingly difficult.

For Version 2, the project aims to redesign the application with a stronger focus on maintainability, testability, scalability, and separation of concerns. Therefore, a new architectural approach is required.

---

## Decision

Mindshub V2 will adopt Clean Architecture as its primary architectural style.

The application will be organized into independent business modules, each following the principles of Clean Architecture to isolate business rules from framework and infrastructure concerns.

This decision aims to improve maintainability, testability, scalability, and the long-term evolution of the project.

---

## Rationale

Clean Architecture was chosen because it promotes a clear separation of concerns by isolating business rules from frameworks and external technologies.

This architectural style enables the domain layer to remain independent of infrastructure, making the application easier to test, maintain, and evolve over time.

Additionally, Mindshub V2 is expected to grow into a modular platform with multiple business domains. Adopting Clean Architecture from the beginning provides a solid foundation for long-term scalability while reducing coupling between components.

---

## Consequences

### Positive

- Business rules remain independent of frameworks and infrastructure.
- Improved maintainability through clear separation of responsibilities.
- Increased testability, especially for domain and application layers.
- Easier adoption of new technologies without impacting the core business logic.
- Better scalability as new business modules are introduced.
- Greater consistency across the project structure.

### Negative

- Increased architectural complexity compared to traditional MVC applications.
- More boilerplate code due to the separation into multiple layers.
- Higher learning curve for new contributors unfamiliar with Clean Architecture.
- Development may require more upfront planning before implementing features.