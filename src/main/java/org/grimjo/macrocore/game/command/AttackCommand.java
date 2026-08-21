package org.grimjo.macrocore.game.command;

import lombok.Value;
import org.grimjo.macrocore.infrastructure.bus.Command;

@Value
public class AttackCommand implements Command {
    Long attackerId;
    Long targetId;
}
