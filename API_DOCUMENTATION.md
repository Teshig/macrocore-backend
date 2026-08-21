# Macrocore Backend API Documentation

This document describes the interaction between `macrocore-frontend` and `macrocore-backend`. The transport layer is divided into two parts:
1. **REST API** — for obtaining the initial state upon loading or transitioning between locations.
2. **WebSocket (STOMP)** — for real-time bidirectional communication (sending commands, receiving events).

---

## 1. REST API

Base URL: `http://<backend-host>:<port>`

### Get Player (NPC) State
Returns basic information about the player, their attributes, and current location.
*   **Endpoint:** `GET /api/game/player/{playerId}`
*   **Path Parameters:**
    *   `playerId` (Long) — unique player identifier.
*   **Response (Success 200 OK):**
    ```json
    {
      "id": { "value": 1 },
      "name": "PlayerName",
      "description": "Description",
      "zoneId": { "value": 100 },
      "roomId": { "value": 101 },
      "health": 100,
      "hunger": 0,
      "money": 50,
      "status": "ALIVE",
      "actionQueue": [],
      "settlementId": null,
      "currentOrderId": null,
      "consumption": 1,
      "dead": false
    }
    ```
*   **Response (Error 404 Not Found):** If the player is not found.

### Get Room State
Returns static and dynamic data about the current room (name, description, available exits).
*   **Endpoint:** `GET /api/game/room/{roomId}`
*   **Path Parameters:**
    *   `roomId` (Long) — unique room identifier.
*   **Response (Success 200 OK):**
    ```json
    {
      "id": { "value": 101 },
      "zoneId": { "value": 100 },
      "name": "Town Square",
      "description": "The central square of the town.",
      "exits": {
        "NORTH": { "zoneId": 100, "roomId": 102 },
        "SOUTH": { "zoneId": 100, "roomId": 100 }
      }
    }
    ```
*   **Response (Error 404 Not Found):** If the room is not found.

---

## 2. WebSocket (STOMP) API

All real-time game interactions happen over the WebSockets protocol using STOMP.

*   **Connection URL:** `ws://<backend-host>:<port>/ws`
*   **SockJS** fallback is supported (you can use `http://<backend-host>:<port>/ws` via the `sockjs-client` library).
*   **CORS:** All origins (`*`) are allowed.

### 2.1. Event Subscription (Receiving data from the server)

The frontend must subscribe to STOMP topics to receive game state updates.

#### Global Events
For events concerning the entire world (e.g., system notifications, global chat).
*   **Topic:** `/topic/events`

#### Local Events (Room Events)
Events that occur in a specific room (movements of other players, local chat, combats). The frontend should subscribe to the topic of the room where the player is currently located.
*   **Topic:** `/topic/rooms/{roomId}`
*   **Example Payload (RoomChatEvent):**
    ```json
    {
      "roomId": { "value": 101 },
      "speakerId": { "value": 1 },
      "message": "Hello everyone!"
    }
    ```
*(Note: the structure of other events, such as movement or attack, will be similar, implementing the `Event` interface)*

---

### 2.2. Sending Commands (Player Actions)

Commands are sent by the client to the server via STOMP with the `/app/...` prefix.

#### Move
A request to move the player in a specific direction.
*   **Destination:** `/app/commands/move`
*   **Payload:**
    ```json
    {
      "actorId": 1,
      "direction": "NORTH"
    }
    ```
*(Allowed directions depend on the available exits in the room: NORTH, SOUTH, EAST, WEST, etc.)*

#### Attack
A request to attack another NPC/player.
*   **Destination:** `/app/commands/attack`
*   **Payload:**
    ```json
    {
      "attackerId": 1,
      "targetId": 2
    }
    ```

#### Chat
Sending a message to the local room chat.
*   **Destination:** `/app/commands/chat`
*   **Payload:**
    ```json
    {
      "speakerId": 1,
      "message": "Hello world!"
    }
    ```

---

## Frontend Usage Scenario Example
1. The user enters the game (passing their `playerId`).
2. The frontend makes a REST request `GET /api/game/player/{playerId}`, retrieving their status and `roomId` (e.g., `101`).
3. The frontend makes a REST request `GET /api/game/room/101`, loading the room description and available exits to render the UI.
4. The frontend establishes a WebSocket (STOMP) connection to `/ws`.
5. The client subscribes to `/topic/rooms/101` (to see the local chat and other players) and `/topic/events` (for system messages).
6. The player writes in the chat. The client sends a STOMP message to `/app/commands/chat` with `speakerId=1` and the message text.
7. The server processes the command, generates a `RoomChatEvent` and broadcasts it to `/topic/rooms/101`.
8. The frontend receives the event via the subscription and displays the message in the chat UI.
9. The player clicks the "North" button. The client sends `/app/commands/move` (`direction: "NORTH"`).
10. Upon successful transition, the player receives a new state, unsubscribes from `/topic/rooms/101`, and subscribes to the new room.