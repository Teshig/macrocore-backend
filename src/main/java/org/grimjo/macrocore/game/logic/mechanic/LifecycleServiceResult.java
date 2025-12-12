package org.grimjo.macrocore.game.logic.mechanic;


import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.item.Corpse;

@Value
@Builder
public class LifecycleServiceResult {
  List<NpcBase> survivors;
  List<Corpse> corpses;
}
