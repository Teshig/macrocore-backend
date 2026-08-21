---
apply: always
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
