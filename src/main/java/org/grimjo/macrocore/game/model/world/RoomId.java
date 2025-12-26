package org.grimjo.macrocore.game.model.world;

import lombok.Value;

@Value(staticConstructor = "of")
public class RoomId {
  String value;
}
