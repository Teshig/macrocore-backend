package org.grimjo.macrocore.infrastructure.bus;

/**
 * Обработчик конкретной команды.
 */
public interface CommandHandler<C extends Command> {
    
    /**
     * Выполняет команду.
     */
    void handle(C command);
    
    /**
     * Возвращает тип команды, который поддерживает этот обработчик.
     */
    Class<C> getCommandType();
}
