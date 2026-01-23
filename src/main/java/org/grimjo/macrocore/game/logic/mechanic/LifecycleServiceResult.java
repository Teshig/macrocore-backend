package org.grimjo.macrocore.game.logic.mechanic;


import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.object.Corpse;

@Value
@Builder
public class LifecycleServiceResult {
  List<NpcBase> survivors;
  List<Corpse> corpses;
}
