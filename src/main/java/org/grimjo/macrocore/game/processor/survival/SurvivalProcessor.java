package org.grimjo.macrocore.game.processor.survival;

import java.util.Collection;
import java.util.stream.Collector;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.actor.NpcStatus;

@Builder
@RequiredArgsConstructor
public class SurvivalProcessor {
  private static final int HUNGER_DAMAGE = 1;

  public SurvivalProcessorResult processAll(Collection<NpcBase> population) {
    return population.stream()
        .map(this::applyDailySurvivalEffects)
        .collect(
            Collector.of(
                SurvivalProcessorAccumulator::new,
                SurvivalProcessorAccumulator::accept,
                SurvivalProcessorAccumulator::combine,
                SurvivalProcessorAccumulator::toResult,
                Collector.Characteristics.UNORDERED,
                Collector.Characteristics.CONCURRENT));
  }

  private NpcBase applyDailySurvivalEffects(NpcBase npc) {
    if (npc.isDead()) {
      return npc;
    }

    int currentHunger = npc.getHunger();
    int currentHealth = npc.getHealth();

    int nextHunger = Math.min(100, currentHunger + NpcBase.DAILY_CONSUMPTION);

    int damage = (nextHunger >= 50) ? HUNGER_DAMAGE : 0;
    int nextHealth = currentHealth - damage;

    if (nextHealth <= 0) {
      return npc.toBuilder().hunger(nextHunger).health(nextHealth).status(NpcStatus.DEAD).build();
    }

    return npc.toBuilder().hunger(nextHunger).health(nextHealth).build();
  }
}
