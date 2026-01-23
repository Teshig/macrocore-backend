package org.grimjo.macrocore.game.domain.object;

import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;

@Value
@Builder
public class Item {
  long id;
  RoomId roomId;
  String name;
}
