package org.grimjo.macrocore.infrastructure.bus;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SimpleCommandBus implements CommandBus {

    private final Map<Class<? extends Command>, CommandHandler<? extends Command>> handlers;

    public SimpleCommandBus(List<CommandHandler<? extends Command>> handlerList) {
        // При запуске Spring собирает все бины CommandHandler и мы мапим их по типу команды
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(CommandHandler::getCommandType, Function.identity()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Command> void dispatch(C command) {
        CommandHandler<C> handler = (CommandHandler<C>) handlers.get(command.getClass());
        
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for command: " + command.getClass().getName());
        }
        
        handler.handle(command);
    }
}
