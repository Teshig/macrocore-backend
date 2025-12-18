package org.grimjo.macrocore.game.engine;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.model.global.WorldState;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;
import org.grimjo.macrocore.game.processor.decay.DecayProcessor;
import org.grimjo.macrocore.game.processor.decay.DecayProcessorContext;
import org.grimjo.macrocore.game.processor.decay.DecayProcessorResult;
import org.grimjo.macrocore.game.processor.settlement.SettlementStateProcessor;
import org.grimjo.macrocore.game.processor.survival.SurvivalProcessor;
import org.grimjo.macrocore.game.processor.survival.SurvivalProcessorResult;

@Slf4j
@Builder
@RequiredArgsConstructor
public class GameEngine {
  private final SettlementStateProcessor settlementProcessor;
  private final SurvivalProcessor survivalProcessor;
  private final DecayProcessor decayProcessor;

  public WorldState processTick(WorldState currentWorld) {
    long nextTick = currentWorld.getTick() + 1;

    SurvivalProcessorResult survivalResult = survivalProcessor.processAll(currentWorld.getPopulation().values());

    DecayProcessorContext decayContext = DecayProcessorContext.builder()
        .nextTick(nextTick)
        .currentCorpses(currentWorld.getCorpses())
        .currentPurgeSchedule(currentWorld.getPurgeSchedule())
        .newlyDeadNpcs(survivalResult.getDead().values())
        .build();
    DecayProcessorResult decayResult = decayProcessor.process(decayContext);

    Map<Long, SmallSettlement> nextSettlements = currentWorld.getSettlements().values()
        .parallelStream()
        .map(settlementProcessor::process)
        .collect(Collectors.toMap(
            SmallSettlement::getId,
            Function.identity()
        ));

    return currentWorld.toBuilder()
        .tick(nextTick)
        .settlements(nextSettlements)
        .population(survivalResult.getAlive())
        .corpses(decayResult.getCorpses())
        .purgeSchedule(decayResult.getPurgeSchedule())
        .build();
  }
}

