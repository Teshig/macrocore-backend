package org.grimjo.macrocore.game.model.global;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.object.Corpse;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;
import org.grimjo.macrocore.game.model.world.Room;
import org.grimjo.macrocore.game.model.world.RoomId;

@Value
@Builder(toBuilder = true)
public class WorldState {
  long tick;

  Map<RoomId, Room> rooms;
  Map<String, NpcBase> population;
  Map<Long, SmallSettlement> settlements;
  @Default Map<RoomId, List<Corpse>> corpses = Map.of();;
  @Default SortedMap<Long, List<String>> purgeSchedule = Collections.emptySortedMap();;
}
