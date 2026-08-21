package org.grimjo.macrocore.infrastructure.api.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.event.RoomChatEvent;
import org.grimjo.macrocore.infrastructure.bus.Event;
import org.grimjo.macrocore.infrastructure.bus.EventHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventBroadcasterService implements EventHandler<Event> {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void handle(Event event) {
        if (event instanceof RoomChatEvent chatEvent) {
            String topic = "/topic/rooms/" + chatEvent.getRoomId().getValue();
            log.debug("Broadcasting RoomChatEvent to {}: {}", topic, event);
            messagingTemplate.convertAndSend(topic, event);
        } else {
            String topic = "/topic/events";
            log.debug("Broadcasting global Event to {}: {}", topic, event);
            messagingTemplate.convertAndSend(topic, event);
        }
    }

    @Override
    public Class<Event> getEventType() {
        return Event.class;
    }
}
