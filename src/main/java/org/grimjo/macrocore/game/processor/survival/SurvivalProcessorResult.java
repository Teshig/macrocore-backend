package org.grimjo.macrocore.game.processor.survival;

import java.util.Map;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;

@Value
@Builder
public class SurvivalProcessorResult {
  @Default Map<String, NpcBase> alive = Map.of();
  @Default Map<String, NpcBase> dead = Map.of();
}
