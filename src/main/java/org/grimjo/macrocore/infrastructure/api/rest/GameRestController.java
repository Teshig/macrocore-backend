package org.grimjo.macrocore.infrastructure.api.rest;

import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.domain.actor.Player;
import org.grimjo.macrocore.game.domain.actor.Player.PlayerId;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;
import org.grimjo.macrocore.infrastructure.state.partition.PartitionedStateRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameRestController {

    private final PartitionedStateRegistry stateRegistry;

    @GetMapping("/player/{playerId}")
    public ResponseEntity<Player> getPlayerState(@PathVariable Long playerId) {
        Player player = stateRegistry.getPlayer(PlayerId.of(playerId));
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(player);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Room> getRoomState(@PathVariable Long roomId) {
        Room room = stateRegistry.getRoom(RoomId.of(roomId));
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }
}
