package org.grimjo.macrocore.infrastructure.bus;

/**
 * Шина для диспетчеризации команд их обработчикам.
 */
public interface CommandBus {
    
    /**
     * Отправляет команду на выполнение соответствующему обработчику.
     */
    <C extends Command> void dispatch(C command);
}
