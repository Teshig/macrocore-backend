package org.grimjo.macrocore.game.command;

import lombok.Value;
import org.grimjo.macrocore.infrastructure.bus.Command;

@Value
public class ChatCommand implements Command {
    Long speakerId;
    String message;
}
