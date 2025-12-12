package org.grimjo.macrocore.game.logic.mechanic;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.actor.NpcStatus;

@RequiredArgsConstructor
public class SurvivalService {
  public SurvivalResult processDailySurvival(List<NpcBase> population, long foodStock) {
    List<NpcBase> updatedPopulation = new ArrayList<>(population.size());
    long currentFood = foodStock;

    for (NpcBase npc : population) {
      if (npc.isDead()) {
        updatedPopulation.add(npc);
        continue;
      }

      long needed = npc.getConsumption();
      int nextHunger;

      if (currentFood >= needed) {
        currentFood -= needed;
        nextHunger = 0;
      } else {
        nextHunger = Math.min(100, npc.getHunger() + 10);
      }

      var builder = npc.toBuilder().hunger(nextHunger);

      if (nextHunger >= 100) {
        builder.status(NpcStatus.DEAD);
      }

      updatedPopulation.add(builder.build());
    }

    return new SurvivalResult(updatedPopulation, currentFood);
  }

  public record SurvivalResult(
      List<NpcBase> survivors,
      long remainingFood
  ) {}
}
