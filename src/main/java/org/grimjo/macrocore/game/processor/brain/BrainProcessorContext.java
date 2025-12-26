package org.grimjo.macrocore.game.processor.brain;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.global.WorldState;
import org.grimjo.macrocore.game.model.object.Item;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;
import org.grimjo.macrocore.game.model.world.Room;
import org.grimjo.macrocore.game.model.world.RoomId;

@Value
@Builder
public class BrainProcessorContext {
  Map<String, NpcBase> population;
  Map<RoomId, Room> rooms;

  Map<String, SmallSettlement> settlements;

  Map<RoomId, List<Item>> groundItems;

  public static BrainProcessorContext from(WorldState world, Map<String, NpcBase> alivePopulation) {
    return BrainProcessorContext.builder()
        .population(alivePopulation)
        .rooms(world.getRooms())
        .settlements(world.getSettlements())
        .build();
  }
}