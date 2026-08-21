package org.grimjo.macrocore.game.event;

import lombok.Value;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;
import org.grimjo.macrocore.infrastructure.bus.Event;

@Value
public class RoomChatEvent implements Event {
    RoomId roomId;
    Long speakerId;
    String message;
}
