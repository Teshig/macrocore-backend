package org.grimjo.macrocore.game.logic.mechanic;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;

@Value
@Builder
public class SurvivalServiceResult {
  List<NpcBase> survivors;
  long remainingFood;
}
