---
apply: always
---

# Architecture Rules (Macrocore Project)

This project strictly follows the **Hexagonal Architecture (Ports and Adapters)** principles.
We strongly separate the "Clean Core" (Game Logic) from the "Infrastructure Shell" (Spring Boot, Database, Web, etc.).

## 1. Clean Core (`org.grimjo.macrocore.game`)
- **Isolation:** The `game` package must NOT contain any infrastructure-specific dependencies.
  - NO Spring context annotations (`@Service`, `@Component`, `@Autowired`, etc.).
  - NO JPA annotations (`@Entity`, `@Table`, etc.).
  - NO Web annotations (`@RestController`, etc.).
- **Immutability:** Domain Model classes must be immutable. Use `@Value` and `@Builder(toBuilder = true)` from Lombok.
- **State Changes:** To change state, create new instances via `.toBuilder()`, never mutate existing ones.
- **Service Design:** Services (Mechanics/Policies) should be pure functions where possible. They must accept narrow contexts (DTOs) or specific lists, never the entire massive `Settlement` or `WorldState` objects, to prevent accidental side effects.

## 2. Infrastructure Shell (`org.grimjo.macrocore.infrastructure`)
- **Dependency Flow:** Infrastructure knows about the Game core, but the Game core DOES NOT know about Infrastructure.
- **Adapters & Mappers:** Converters (Mappers) are placed in the `adapter` or `mapper` packages. They adapt Domain objects to DB Entities or API DTOs.
