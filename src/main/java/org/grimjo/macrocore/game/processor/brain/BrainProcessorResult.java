package org.grimjo.macrocore.game.processor.brain;

import java.util.Map;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;

@Value
@Builder
public class BrainProcessorResult {
  Map<NpcId, NpcBase> updatedNpcs;
}