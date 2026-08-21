package org.grimjo.macrocore.game.command;

import lombok.Value;
import org.grimjo.macrocore.game.domain.world.Direction;
import org.grimjo.macrocore.infrastructure.bus.Command;

@Value
public class MoveCommand implements Command {
    Long actorId;
    Direction direction;
}
