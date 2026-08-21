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

import org.grimjo.macrocore.infrastructure.state.partition.PartitionedStateRegistry;

@Slf4j
@Builder
@RequiredArgsConstructor
public class GameEngine {
  private final SettlementStateProcessor settlementProcessor;
  private final SurvivalProcessor survivalProcessor;
  private final DecayProcessor decayProcessor;
  private final BrainProcessor brainProcessor;
  private final TaskExecutionProcessor taskProcessor;
  private final PartitionedStateRegistry partitionedRegistry;

  public WorldState processTick(WorldState currentWorld) {
    long nextTick = currentWorld.getTick() + 1;

    // 1. Берем иммутабельные копии из реестра вместо WorldState
    Map<NpcId, NpcBase> currentPopulation = partitionedRegistry.getAllNpcs().stream()
        .collect(Collectors.toMap(NpcBase::getId, Function.identity()));

    SurvivalProcessorResult survivalResult = survivalProcessor.processAll(currentPopulation.values());
    
    // Обновляем мертвых в реестре
    survivalResult.getDead().values().forEach(deadNpc -> 
        partitionedRegistry.updateNpc(deadNpc.getId(), old -> deadNpc)
    );

    // Только живые продолжают думать
    Map<NpcId, NpcBase> alivePopulation = new HashMap<>(survivalResult.getAlive());

    BrainProcessorContext brainContext = BrainProcessorContext.builder()
        .population(alivePopulation)
        .rooms(partitionedRegistry.getAllRooms().stream().collect(Collectors.toMap(org.grimjo.macrocore.game.domain.world.Room::getId, Function.identity())))
        .settlements(partitionedRegistry.getAllSettlements().stream().collect(Collectors.toMap(SmallSettlement::getId, Function.identity())))
        .build();

    BrainProcessorResult brainResult = brainProcessor.process(brainContext);

    // Обновляем результаты раздумий в реестре
    brainResult.getUpdatedNpcs().values().forEach(updatedNpc -> {
        partitionedRegistry.updateNpc(updatedNpc.getId(), old -> updatedNpc);
        alivePopulation.put(updatedNpc.getId(), updatedNpc);
    });

    TaskExecutionResult taskResult = taskProcessor.process(alivePopulation);

    // Обновляем результаты задач в реестре
    taskResult.getUpdatedNpcs().values().forEach(updatedNpc -> {
        partitionedRegistry.updateNpc(updatedNpc.getId(), old -> updatedNpc);
    });
    
    List<SettlementTransaction> transactions = taskResult.getTransactions();

    DecayProcessorContext decayContext = DecayProcessorContext.builder()
        .nextTick(nextTick)
        .currentCorpses(currentWorld.getCorpses())
        .currentPurgeSchedule(currentWorld.getPurgeSchedule())
        .newlyDeadNpcs(survivalResult.getDead().values())
        .build();
    DecayProcessorResult decayResult = decayProcessor.process(decayContext);

    // Обновляем поселения
    partitionedRegistry.getAllSettlements().parallelStream().forEach(settlement -> {
        partitionedRegistry.updateSettlement(settlement.getId(), old -> 
            settlementProcessor.process(old, transactions)
        );
    });

    // Возвращаем обновленный "глобальный" стейт для сохранения тика
    return currentWorld.toBuilder()
        .tick(nextTick)
        .corpses(decayResult.getCorpses())
        .purgeSchedule(decayResult.getPurgeSchedule())
        .build();
  }
}

