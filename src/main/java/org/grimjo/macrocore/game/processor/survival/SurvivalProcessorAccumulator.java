package org.grimjo.macrocore.game.processor.survival;

import java.util.HashMap;
import java.util.Map;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.actor.NpcStatus;

@Value
public class SurvivalProcessorAccumulator {
  Map<String, NpcBase> alive = new HashMap<>();
  Map<String, NpcBase> dead = new HashMap<>();

  public void accept(NpcBase npc) {
    if (npc.getStatus() == NpcStatus.ALIVE) {
      alive.put(npc.getId(), npc);
    } else {
      dead.put(npc.getId(), npc);
    }
  }

  public SurvivalProcessorAccumulator combine(SurvivalProcessorAccumulator other) {
    this.alive.putAll(other.alive);
    this.dead.putAll(other.dead);
    return this;
  }

  public SurvivalProcessorResult toResult() {
    return SurvivalProcessorResult.builder()
        .alive(Map.copyOf(alive))
        .dead(Map.copyOf(dead))
        .build();
  }
}
