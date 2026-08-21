package org.grimjo.macrocore.infrastructure.bus;

/**
 * Обработчик конкретного события.
 */
public interface EventHandler<E extends Event> {
    
    /**
     * Обрабатывает произошедшее событие.
     */
    void handle(E event);
    
    /**
     * Возвращает тип события, на которое подписан обработчик.
     */
    Class<E> getEventType();
}
