package org.grimjo.macrocore.game.model.action;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Action {
  ActionType type;
  String targetId;

  @Builder.Default int duration = 1;
}