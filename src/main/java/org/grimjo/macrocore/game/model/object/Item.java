package org.grimjo.macrocore.game.model.object;

import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.model.world.RoomId;

@Value
@Builder
public class Item {
  long id;
  RoomId roomId;
  String name;
}
