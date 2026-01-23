package org.grimjo.macrocore.game.domain.object;

import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;

@Value
@Builder
public class Corpse {
  String id;
  NpcId originalNpcId;
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
