package org.grimjo.macrocore.game.processor.decay;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.object.Corpse;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;

@Value
@Builder
public class DecayProcessorContext {
  long nextTick;

  Map<RoomId, List<Corpse>> currentCorpses;
  SortedMap<Long, List<String>> currentPurgeSchedule;

  Collection<NpcBase> newlyDeadNpcs;
}
