package org.grimjo.macrocore.game.processor.decay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.object.Corpse;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;
import org.grimjo.macrocore.game.utils.ImmutabilityUtils;

@Builder
@RequiredArgsConstructor
public class DecayProcessor {
  private static final long CORPSE_DECAY_TIME = 30L;

  public DecayProcessorResult process(DecayProcessorContext context) {
    long nextTick = context.getNextTick();
    SortedMap<Long, List<String>> purgeSchedule = context.getCurrentPurgeSchedule();
    SortedMap<Long, List<String>> expiredEntries = purgeSchedule.headMap(nextTick);
    Set<String> idsToRemove = expiredEntries.values().stream()
        .flatMap(List::stream)
        .collect(Collectors.toSet());

    Map<RoomId, List<Corpse>> nextCorpses = context.getCurrentCorpses().entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue().stream()
                .filter(c -> !idsToRemove.contains(c.getId()))
                .collect(Collectors.toCollection(ArrayList::new))
        ));

    SortedMap<Long, List<String>> nextPurgeSchedule = new TreeMap<>(purgeSchedule.tailMap(nextTick));
    context.getNewlyDeadNpcs().forEach(deadNpc -> {
      long decayTick = nextTick + CORPSE_DECAY_TIME;
      Corpse corpse = createCorpse(deadNpc, decayTick);
      nextCorpses.computeIfAbsent(deadNpc.getRoomId(), k -> new ArrayList<>())
          .add(corpse);

      nextPurgeSchedule.computeIfAbsent(decayTick, k -> new ArrayList<>())
          .add(corpse.getId());
    });

    return DecayProcessorResult.builder()
        .corpses(ImmutabilityUtils.freezeMap(nextCorpses))
        .purgeSchedule(ImmutabilityUtils.freezeSortedMap(nextPurgeSchedule))
        .build();
  }

  private Corpse createCorpse(NpcBase npc, long decayTick) {
    return Corpse.builder()
        .id(UUID.randomUUID().toString())
        .name("Corpse (" + npc.getName() + ")")
        .originalNpcId(npc.getId())
        .roomId(npc.getRoomId())
        .decayTick(decayTick)
        .build();
  }
}
