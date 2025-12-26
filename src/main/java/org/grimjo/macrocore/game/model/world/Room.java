package org.grimjo.macrocore.game.model.world;

import java.util.Map;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class Room {
  RoomId id;
  String regionId;

  String name;
  String description;

  @Singular
  Map<Direction, RoomId> exits;
}
