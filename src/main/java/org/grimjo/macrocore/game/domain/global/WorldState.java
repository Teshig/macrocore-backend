package org.grimjo.macrocore.game.domain.global;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.object.Corpse;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement.SettlementId;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;

@Value
@Builder(toBuilder = true)
public class WorldState {
  long tick;

  Map<RoomId, Room> rooms;
  Map<NpcId, NpcBase> population;
  Map<SettlementId, SmallSettlement> settlements;
  @Default Map<RoomId, List<Corpse>> corpses = Map.of();
  @Default SortedMap<Long, List<String>> purgeSchedule = Collections.emptySortedMap();
}
