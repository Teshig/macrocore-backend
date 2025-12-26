package org.grimjo.macrocore.game.model.object;

import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.world.RoomId;

@Value
@Builder
public class Corpse {
  String id;
  String originalNpcId;
  RoomId roomId;
  String name;
  long decayTick;

  public static Corpse from(NpcBase npc) {
    return Corpse.builder()
        .originalNpcId(npc.getId())
        .name(npc.getName())
        .build();
  }
}
