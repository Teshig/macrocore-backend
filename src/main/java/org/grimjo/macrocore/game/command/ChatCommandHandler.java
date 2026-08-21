package org.grimjo.macrocore.game.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.domain.actor.Player;
import org.grimjo.macrocore.game.domain.actor.Player.PlayerId;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;
import org.grimjo.macrocore.game.event.RoomChatEvent;
import org.grimjo.macrocore.infrastructure.bus.CommandHandler;
import org.grimjo.macrocore.infrastructure.bus.EventBus;
import org.grimjo.macrocore.infrastructure.state.partition.PartitionedStateRegistry;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatCommandHandler implements CommandHandler<ChatCommand> {

    private final PartitionedStateRegistry registry;
    private final EventBus eventBus;

    @Override
    public void handle(ChatCommand command) {
        RoomId speakerRoomId = getActorRoomId(command.getSpeakerId());
        if (speakerRoomId == null) {
            return;
        }

        RoomChatEvent chatEvent = new RoomChatEvent(
                speakerRoomId,
                command.getSpeakerId(),
                command.getMessage()
        );
        eventBus.publish(chatEvent);

        log.info("Actor {} says in room {}: {}", command.getSpeakerId(), speakerRoomId.getValue(), command.getMessage());
    }

    private RoomId getActorRoomId(Long id) {
        Player player = registry.getPlayer(PlayerId.of(id));
        if (player != null && !player.isDead()) {
            return player.getRoomId();
        }
        NpcBase npc = registry.getNpc(NpcId.of(id));
        if (npc != null && !npc.isDead()) {
            return npc.getRoomId();
        }
        return null;
    }

    @Override
    public Class<ChatCommand> getCommandType() {
        return ChatCommand.class;
    }
}
