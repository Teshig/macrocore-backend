package org.grimjo.macrocore.infrastructure.api.websocket;

import org.grimjo.macrocore.game.command.AttackCommand;
import org.grimjo.macrocore.game.command.ChatCommand;
import org.grimjo.macrocore.game.command.MoveCommand;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.world.Direction;
import org.grimjo.macrocore.infrastructure.api.websocket.dto.AttackCommandRequest;
import org.grimjo.macrocore.infrastructure.api.websocket.dto.ChatCommandRequest;
import org.grimjo.macrocore.infrastructure.api.websocket.dto.MoveCommandRequest;
import org.grimjo.macrocore.infrastructure.bus.CommandBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommandMessageControllerTest {

    @Mock
    private CommandBus commandBus;

    @InjectMocks
    private CommandMessageController controller;

    @Test
    void shouldHandleMoveCommand() {
        MoveCommandRequest request = new MoveCommandRequest();
        request.setActorId(1L);
        request.setDirection("NORTH");

        controller.handleMoveCommand(request);

        ArgumentCaptor<MoveCommand> captor = ArgumentCaptor.forClass(MoveCommand.class);
        verify(commandBus).dispatch(captor.capture());

        MoveCommand command = captor.getValue();
        assertThat(command.getActorId()).isEqualTo(1L);
        assertThat(command.getDirection()).isEqualTo(Direction.NORTH);
    }

    @Test
    void shouldHandleAttackCommand() {
        AttackCommandRequest request = new AttackCommandRequest();
        request.setAttackerId(1L);
        request.setTargetId(2L);

        controller.handleAttackCommand(request);

        ArgumentCaptor<AttackCommand> captor = ArgumentCaptor.forClass(AttackCommand.class);
        verify(commandBus).dispatch(captor.capture());

        AttackCommand command = captor.getValue();
        assertThat(command.getAttackerId()).isEqualTo(1L);
        assertThat(command.getTargetId()).isEqualTo(2L);
    }

    @Test
    void shouldHandleChatCommand() {
        ChatCommandRequest request = new ChatCommandRequest();
        request.setSpeakerId(1L);
        request.setMessage("Hello");

        controller.handleChatCommand(request);

        ArgumentCaptor<ChatCommand> captor = ArgumentCaptor.forClass(ChatCommand.class);
        verify(commandBus).dispatch(captor.capture());

        ChatCommand command = captor.getValue();
        assertThat(command.getSpeakerId()).isEqualTo(1L);
        assertThat(command.getMessage()).isEqualTo("Hello");
    }
}
