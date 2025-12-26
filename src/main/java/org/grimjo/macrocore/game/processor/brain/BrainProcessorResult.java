package org.grimjo.macrocore.game.processor.brain;

import java.util.Map;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;

@Value
@Builder
public class BrainProcessorResult {
  Map<String, NpcBase> updatedNpcs;
}