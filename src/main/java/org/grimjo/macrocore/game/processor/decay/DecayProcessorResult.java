package org.grimjo.macrocore.game.processor.decay;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.model.object.Corpse;
import org.grimjo.macrocore.game.model.world.RoomId;

@Value
@Builder
public class DecayProcessorResult {
  Map<RoomId, List<Corpse>> corpses;
  SortedMap<Long, List<String>> purgeSchedule;
}
