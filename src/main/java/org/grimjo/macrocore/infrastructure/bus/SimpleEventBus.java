package org.grimjo.macrocore.infrastructure.bus;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleEventBus implements EventBus {

    private final List<EventHandler<? extends Event>> handlers;

    public SimpleEventBus(List<EventHandler<? extends Event>> handlers) {
        this.handlers = handlers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Event> void publish(E event) {
        handlers.stream()
                .filter(h -> h.getEventType().isAssignableFrom(event.getClass()))
                .map(h -> (EventHandler<E>) h)
                .forEach(h -> h.handle(event));
    }
}
