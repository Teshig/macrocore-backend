package org.grimjo.macrocore.infrastructure.api.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.command.AttackCommand;
import org.grimjo.macrocore.game.command.ChatCommand;
import org.grimjo.macrocore.game.command.MoveCommand;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.world.Direction;
import org.grimjo.macrocore.infrastructure.api.websocket.dto.AttackCommandRequest;
import org.grimjo.macrocore.infrastructure.api.websocket.dto.ChatCommandRequest;
import org.grimjo.macrocore.infrastructure.api.websocket.dto.MoveCommandRequest;
import org.grimjo.macrocore.infrastructure.bus.CommandBus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommandMessageController {

    private final CommandBus commandBus;

    @MessageMapping("/commands/move")
    public void handleMoveCommand(MoveCommandRequest request) {
        log.debug("Received move command: {}", request);
        try {
            MoveCommand command = new MoveCommand(
                    request.getActorId(),
                    Direction.valueOf(request.getDirection().toUpperCase())
            );
            commandBus.dispatch(command);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid move command direction: {}", request.getDirection());
        } catch (Exception e) {
            log.error("Error processing move command", e);
        }
    }

    @MessageMapping("/commands/attack")
    public void handleAttackCommand(AttackCommandRequest request) {
        log.debug("Received attack command: {}", request);
        try {
            AttackCommand command = new AttackCommand(
                    request.getAttackerId(),
                    request.getTargetId()
                );
            commandBus.dispatch(command);
        } catch (Exception e) {
            log.error("Error processing attack command", e);
        }
    }

    @MessageMapping("/commands/chat")
    public void handleChatCommand(ChatCommandRequest request) {
        log.debug("Received chat command: {}", request);
        try {
            ChatCommand command = new ChatCommand(
                    request.getSpeakerId(),
                    request.getMessage()
                );
            commandBus.dispatch(command);
        } catch (Exception e) {
            log.error("Error processing chat command", e);
        }
    }
}
