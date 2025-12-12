# Project Architecture (Macrocore)

The project follows the **Hexagonal Architecture (Ports and Adapters)** principles.
We strictly separate the "Clean Core" (Game Logic) from the "Infrastructure Shell" (Spring Boot, Database, Web).

## Package Structure & Dependencies

### [ CORE ] `org.grimjo.macrocore.game` 
Pure Java 21 + Lombok.

**Strictly Forbidden Dependencies:** Spring context, JPA, Web/Servlet API, or any infrastructure-specific libraries.

* **`model`** — Immutable Data Structures (DTOs) and State definitions.
  * `actor` — Entities possessing agency or life (`NpcBase`, `Corpse`).
  * `settlement` — Core structural units (`Settlement` interface, `SmallSettlement`).
  * `global` — World state containers (`WorldState`).
  * `politic` — Decision-related data (`Decree`).

* **`logic`** — Pure business logic units.
  * `mechanic` — Stateless calculators. Input -> Output. (`SurvivalService`, `TownAssemblyService`).
  * `policy` — AI decision-making logic (`SurvivalPolicy`). Evaluates context to produce decrees.

* **`processor`** — Business Process Orchestration.
  * `SettlementStateProcessor` — The pipeline that combines mechanics and policies to transition a settlement from State T to State T+1.
  * `SettlementProcessingContext` — A read-only snapshot context for logic services.

* **`engine`** — The Game Loop Driver.
  * `GameEngine` — Manages time flow (ticks) and parallelism. Delegates actual logic to `processor`.

**Dependency Flow:** `engine` -> `processor` -> `logic` -> `model`. (No circular dependencies).

---

### [ SHELL ] `org.grimjo.macrocore.infrastructure`
Glue code based on Spring Boot. This layer depends on the `game` package, but the `game` package never depends on `infrastructure`.

* **`config`** — Spring Beans configuration. This is where we instantiate classes from the `game` package (Dependency Injection root).
* **`persistence`** — Database interactions (PostgreSQL).
    * `entity` — Mutable JPA entities (`SettlementEntity`).
    * `repository` — Spring Data interfaces.
* **`adapter`** — Converters (`Mappers`) between `domain` objects (Game) and `entity` objects (DB).
* **`state`** — In-Memory state management.
    * `InMemoryStateHolder` — Thread-safe container for `WorldState`.
    * `SimulationTicker` — `@Scheduled` trigger that executes the `GameEngine`.
* **`api`** — REST controllers for the Frontend.

### Key Architectural Rules
1.  **Dependency Rule:** Infrastructure knows about the Game. The Game **does not know** about Infrastructure.
2.  **Immutability:** The Domain Model uses immutable objects (`@Value`). State changes are achieved by creating new instances (`toBuilder()`), not by mutating existing ones.
3.  **Segregation:** Services (Mechanics/Policies) accept narrow contexts (DTOs) or specific lists, never the entire `Settlement` object, to prevent accidental side effects.