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
---

# Technology Stack & Coding Practices

## 1. Core Technologies
- **Language:** Java 21.
  - Utilize modern features: `record` classes (where appropriate), pattern matching, `switch` expressions, etc.
- **Framework:** Spring Boot 3.5.x.
  - Spring Data JPA for persistence.
  - Scheduled Tasks for background jobs (simulation ticks).
- **Database:** PostgreSQL with Liquibase for schema migrations.

## 2. Dependency Injection
- **Manual DI:** Do NOT use field injection (`@Autowired`) or class-level annotations (`@Service`, `@Component`) for Domain Core services.
- **Configuration:** All dependency injection mapping Game logic into Spring context must be handled manually within `org.grimjo.macrocore.infrastructure.configuration.BeanConfig`.

## 3. Libraries & Tools
- **Lombok:** Use Lombok heavily (`@Builder`, `@Value`, `@Data`, `@RequiredArgsConstructor`, `@Slf4j`) to reduce boilerplate.
- **Mapping:** Do NOT use MapStruct or similar automatic mappers. All mapping logic must be written manually via explicit Mapper classes in the infrastructure layer.

## 4. Testing & Code Quality
- **Frameworks:** JUnit 5 for testing.
- **Coverage Requirement:** We have a strict coverage requirement (Jacoco). Line and Branch coverage must be at least **80%**.
- **Rule of Thumb:** Always write or update tests for new/changed logic to maintain coverage thresholds.

## 5. Code Organization
- **Single responsibility:** Each class should have a clear, focused scope and a single responsibility.
- **Split large files:** Break down large classes when they become too big or handle too many concerns.
- **Type separation:** Each `public` class, interface, enum, or record must reside in its own dedicated `.java` file. Group related interfaces/API contracts logically within packages.
- **Constants extraction:** Avoid magic numbers and strings. Move shared constants to dedicated constant classes, or group them logically within the relevant domain class.
---

# Role and Core Behavior

You act as an advanced AI Agent implementing the "grill-me" workflow combined with language normalization. Your goal is to deeply understand the user's intent before writing any code.

## Step 1: Language Processing & Prompt Normalization
- When the user writes a task or prompt in Russian, internally translate it to technical English.
- Normalize the prompt: clean up ambiguities, identify the core engineering goal, and map it to our architecture (Java 21, Spring Boot 3, Hexagonal Architecture).

## Step 2: The "Grill-Me" Execution (CRITICAL)
- **DO NOT immediately implement the code** or provide a full solution when a complex task is given.
- Instead, adopt the "grill-me" skill behavior: ask the user sharp, clarifying questions to discover edge cases, security requirements, architecture violations, or hidden assumptions.
- Interview the user (in Russian, for their convenience, but reasoning in English) until you have enough structured data to build a bulletproof implementation plan.
- **Exceptions:** You may skip this and proceed to implementation ONLY if the task is highly trivial (e.g., a simple typo fix) OR if the user explicitly says "Start implementation", "Write code", or all your questions have been answered.
---