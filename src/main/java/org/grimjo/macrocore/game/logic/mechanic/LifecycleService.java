package org.grimjo.macrocore.game.logic.mechanic;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.item.Corpse;

@RequiredArgsConstructor
public class LifecycleService {

  public LifecycleResult processLifecycle(List<NpcBase> mixedPopulation) {
    List<NpcBase> survivors = new ArrayList<>();
    List<Corpse> newCorpses = new ArrayList<>();

    for (NpcBase npc : mixedPopulation) {
      if (npc.isDead()) {
        newCorpses.add(Corpse.from(npc));
      } else {
        survivors.add(npc);
      }
    }

    return new LifecycleResult(survivors, newCorpses);
  }

  public record LifecycleResult(
      List<NpcBase> survivors,
      List<Corpse> newCorpses
  ) {}
}
