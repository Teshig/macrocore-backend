package org.grimjo.macrocore.game.engine;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.logic.TownAssemblyService;
import org.grimjo.macrocore.game.model.Decree;
import org.grimjo.macrocore.game.model.Settlement;
import org.grimjo.macrocore.game.model.WorldState;

@Slf4j
@RequiredArgsConstructor
public class GameEngine {
  private final TownAssemblyService assemblyService;

  public WorldState processTick(WorldState currentWorld) {
    long nextTick = currentWorld.getTick() + 1;

    Map<Long, Settlement> nextSettlements = currentWorld.getSettlements().values()
        .parallelStream()
        .map(this::processSingleSettlement)
        .collect(Collectors.toMap(Settlement::getSettlementId, Function.identity()));

    return WorldState.builder().tick(nextTick).settlements(nextSettlements).build();
  }

  private Settlement processSingleSettlement(Settlement settlement) {
    List<Decree> decrees = assemblyService.holdMeeting(settlement);
    return settlement.updateDecrees(decrees);
  }
}
