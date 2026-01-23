package org.grimjo.macrocore.game.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.global.WorldState;
import org.grimjo.macrocore.game.domain.settlement.SettlementTransaction;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement;
import org.grimjo.macrocore.game.processor.brain.BrainProcessor;
import org.grimjo.macrocore.game.processor.brain.BrainProcessorContext;
import org.grimjo.macrocore.game.processor.brain.BrainProcessorResult;
import org.grimjo.macrocore.game.processor.decay.DecayProcessor;
import org.grimjo.macrocore.game.processor.decay.DecayProcessorContext;
import org.grimjo.macrocore.game.processor.decay.DecayProcessorResult;
import org.grimjo.macrocore.game.processor.settlement.SettlementStateProcessor;
import org.grimjo.macrocore.game.processor.survival.SurvivalProcessor;
import org.grimjo.macrocore.game.processor.survival.SurvivalProcessorResult;
import org.grimjo.macrocore.game.processor.task.TaskExecutionProcessor;
import org.grimjo.macrocore.game.processor.task.TaskExecutionResult;

@Slf4j
@Builder
@RequiredArgsConstructor
public class GameEngine {
  private final SettlementStateProcessor settlementProcessor;
  private final SurvivalProcessor survivalProcessor;
  private final DecayProcessor decayProcessor;
  private final BrainProcessor brainProcessor;
  private final TaskExecutionProcessor taskProcessor;

  public WorldState processTick(WorldState currentWorld) {
    long nextTick = currentWorld.getTick() + 1;

    SurvivalProcessorResult survivalResult = survivalProcessor.processAll(currentWorld.getPopulation().values());
    Map<NpcId, NpcBase> currentPopulation = new HashMap<>(survivalResult.getAlive());

    BrainProcessorContext brainContext = BrainProcessorContext.from(currentWorld, currentPopulation);
    BrainProcessorResult brainResult = brainProcessor.process(brainContext);

    currentPopulation.putAll(brainResult.getUpdatedNpcs());
    TaskExecutionResult taskResult = taskProcessor.process(currentPopulation);

    currentPopulation.putAll(taskResult.getUpdatedNpcs());
    List<SettlementTransaction> transactions = taskResult.getTransactions();

    DecayProcessorContext decayContext = DecayProcessorContext.builder()
        .nextTick(nextTick)
        .currentCorpses(currentWorld.getCorpses())
        .currentPurgeSchedule(currentWorld.getPurgeSchedule())
        .newlyDeadNpcs(survivalResult.getDead().values())
        .build();
    DecayProcessorResult decayResult = decayProcessor.process(decayContext);

    Map<String, SmallSettlement> nextSettlements = currentWorld.getSettlements().values()
        .parallelStream()
        .map(settlement -> {
          return settlementProcessor.process(settlement, transactions);
        })
        .collect(Collectors.toMap(
            SmallSettlement::getId,
            Function.identity()
        ));

    return currentWorld.toBuilder()
        .tick(nextTick)
        .settlements(nextSettlements)
        .population(currentPopulation)
        .corpses(decayResult.getCorpses())
        .purgeSchedule(decayResult.getPurgeSchedule())
        .build();
  }
}

