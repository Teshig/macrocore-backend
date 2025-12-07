# MACROCORE: THE SLAVIC ECONOMY SIMULATION
**Version:** 0.2-draft
**Concept:** Autonomous Economic Simulation with Emergent Behavior

## 1. Vision & Setting
The game simulates the economic evolution of early Slavic settlements (e.g., Drevlyans, Krivichs) during the transition from tribal structures to early feudalism.

**Core Philosophy:** "Indirect Control". The player (or the simulation itself) does not control individual units. Instead, they set **Macro-Parameters** (taxes, laws, priorities), and **Agents** (citizens) react to these incentives based on their own needs.

---

## 2. World Entities

### 2.1. The Settlement (Macro Agent)
A settlement represents a tribe or a village. It acts as a local market and a decision-maker.
* **Attributes:**
    * `Culture`: Modifiers affecting behavior (e.g., `Isolationism`, `Mercantilism`).
    * `Biome`: The geography determining available resources (Forest, River, Plains).
    * `Storage`: Shared stockpile (Tribute/Tax).
* **Logic (The Elder's AI):** * Evaluates total welfare.
    * Adjusts **Local Prices** to stimulate specific production (e.g., "We need more wood -> Buy wood at high price").

### 2.2. The Agent (Micro Agent)
A single inhabitant.
* **State:**
    * `Needs`: Hunger (0-100), Warmth (0-100), Energy (0-100).
    * `Inventory`: Personal goods.
    * `Profession`: Dynamic (Farmer, Woodcutter, Trader).
* **Logic (GOAP - Goal Oriented Action Planning):**
    * Every tick, the agent calculates the utility of possible actions.
    * *Formula:* `Utility = (ExpectedProfit * Skill) - (Effort * Fatigue) + (SocialPriority)`.

### 2.3. Resources
* **FOOD:** Consumed for Hunger. Produced in Plains/River.
* **WOOD:** Consumed for Warmth (Winter) and Construction. Produced in Forest.
* **FURS:** Luxury/Trade item. Produced in Forest.
* **ORE/TOOLS:** Advanced production chain.

---

## 3. Geography & Resources (The "Scattering" Model)

To simulate trade and specialization, resources are NOT distributed equally.

| Land Type (Biome) | High Yield | Low Yield | Tribe Archetype |
| :--- | :--- | :--- | :--- |
| **Deep Forest** | Wood, Furs, Honey | Grain (Farming is hard) | **Drevlyans** (Survivalists) |
| **River Bank** | Fish, Trade (Speed) | Wood (Moderate) | **Krivichs** (Traders) |
| **Steppe/Plains** | Grain, Cattle | Wood, Stone | **Polans** (Farmers/Warriors) |

**Global Map Topology:**
The world is a Graph of Nodes (Settlements).
* `Distance(A, B)` determines trade cost.
* River connections reduce trade cost significantly.

---

## 4. Technical Architecture

### 4.1. Database Schema (Draft)

```sql
TABLE settlement (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50),
    biome VARCHAR(20), -- FOREST, RIVER, PLAINS
    treasury DECIMAL,
    culture_vector JSONB -- {"aggression": 0.5, "trade": 0.9}
);

TABLE settlement_market (
    settlement_id BIGINT,
    resource_code VARCHAR(20),
    buy_price DECIMAL,
    sell_price DECIMAL,
    stock DECIMAL
);

TABLE agent (
    id BIGSERIAL PRIMARY KEY,
    settlement_id BIGINT REFERENCES settlement(id),
    profession VARCHAR(20),
    stats JSONB, -- {"strength": 5, "intellect": 3}
    needs JSONB -- {"hunger": 20, "warmth": 100}
);