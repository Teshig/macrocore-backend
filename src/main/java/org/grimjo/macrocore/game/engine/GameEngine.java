package org.grimjo.macrocore.game.engine;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.processor.SettlementStateProcessor;
import org.grimjo.macrocore.game.model.global.WorldState;
import org.grimjo.macrocore.game.model.settlement.Settlement;

@Slf4j
@RequiredArgsConstructor
public class GameEngine {
  private final SettlementStateProcessor settlementProcessor;

  public WorldState processTick(WorldState currentWorld) {
    long nextTick = currentWorld.getTick() + 1;

    Map<Long, Settlement> nextSettlements = currentWorld.getSettlements().values()
        .parallelStream()
        .map(settlementProcessor::process)
        .collect(Collectors.toMap(
            Settlement::getSettlementId,
            Function.identity()
        ));

    return WorldState.builder().tick(nextTick).settlements(nextSettlements).build();
  }
}
