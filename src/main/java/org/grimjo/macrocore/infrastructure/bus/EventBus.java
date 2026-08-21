package org.grimjo.macrocore.infrastructure.bus;

/**
 * Шина для публикации событий (Publish-Subscribe).
 */
public interface EventBus {
    
    /**
     * Публикует событие, рассылая его всем подписанным обработчикам.
     */
    <E extends Event> void publish(E event);
}
