package org.grimjo.macrocore.game.model.item;

import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;

@Value
@Builder
public class Corpse {
  long id;
  long originalNpcId;
  String name;

  public static Corpse from(NpcBase npc) {
    return Corpse.builder()
        .originalNpcId(npc.getId())
        .name(npc.getName())
        .build();
  }
}
