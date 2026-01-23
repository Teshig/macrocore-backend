package org.grimjo.macrocore.game.processor.survival;

import java.util.Map;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;

@Value
@Builder
public class SurvivalProcessorResult {
  @Default Map<NpcId, NpcBase> alive = Map.of();
  @Default Map<NpcId, NpcBase> dead = Map.of();
}
