package org.grimjo.macrocore.game.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.domain.actor.Player;
import org.grimjo.macrocore.game.domain.actor.Player.PlayerId;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.infrastructure.bus.CommandHandler;
import org.grimjo.macrocore.infrastructure.state.partition.PartitionedStateRegistry;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MoveCommandHandler implements CommandHandler<MoveCommand> {

    private final PartitionedStateRegistry registry;

    @Override
    public void handle(MoveCommand command) {
        Long rawId = command.getActorId();
        
        Player player = registry.getPlayer(PlayerId.of(rawId));
        if (player != null && !player.isDead()) {
            handlePlayerMove(player, command);
            return;
        }

        NpcBase npc = registry.getNpc(NpcId.of(rawId));
        if (npc != null && !npc.isDead()) {
            handleNpcMove(npc, command);
            return;
        }

        log.debug("MoveCommand failed: actor not found or dead ({})", rawId);
    }

    private void handlePlayerMove(Player actor, MoveCommand command) {
        Room currentRoom = registry.getRoom(actor.getRoomId());
        if (currentRoom == null) {
            log.warn("MoveCommand failed: room not found ({})", actor.getRoomId());
            return;
        }

        Room.ExitTarget target = currentRoom.getExits().get(command.getDirection());
        if (target == null) {
            log.debug("MoveCommand failed: no exit in direction {} for room {}", command.getDirection(), currentRoom.getId());
            return;
        }

        registry.updatePlayer(actor.getId(), current -> 
            current.toBuilder()
                   .roomId(Room.RoomId.of(target.getRoomId()))
                   .build()
        );

        log.info("Player {} moved {} to room {}", actor.getId().getValue(), command.getDirection(), target.getRoomId());
    }

    private void handleNpcMove(NpcBase actor, MoveCommand command) {
        Room currentRoom = registry.getRoom(actor.getRoomId());
        if (currentRoom == null) {
            log.warn("MoveCommand failed: room not found ({})", actor.getRoomId());
            return;
        }

        Room.ExitTarget target = currentRoom.getExits().get(command.getDirection());
        if (target == null) {
            log.debug("MoveCommand failed: no exit in direction {} for room {}", command.getDirection(), currentRoom.getId());
            return;
        }

        registry.updateNpc(actor.getId(), current -> 
            current.toBuilder()
                   .roomId(Room.RoomId.of(target.getRoomId()))
                   .build()
        );

        log.info("Npc {} moved {} to room {}", actor.getId().getValue(), command.getDirection(), target.getRoomId());
    }

    @Override
    public Class<MoveCommand> getCommandType() {
        return MoveCommand.class;
    }
}
