package org.grimjo.macrocore.game.processor.brain;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.global.WorldState;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement.SettlementId;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;

import org.grimjo.macrocore.game.domain.object.Item;

@Value
@Builder
public class BrainProcessorContext {
  Map<NpcId, NpcBase> population;
  Map<RoomId, Room> rooms;

  Map<SettlementId, SmallSettlement> settlements;

  Map<RoomId, List<Item>> groundItems;

  public static BrainProcessorContext from(WorldState world, Map<NpcId, NpcBase> alivePopulation) {
    return BrainProcessorContext.builder()
        .population(alivePopulation)
        .rooms(world.getRooms())
        .settlements(world.getSettlements())
        .build();
  }
}