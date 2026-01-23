package org.grimjo.macrocore.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.grimjo.macrocore.game.engine.GameEngine;
import org.grimjo.macrocore.game.logic.mechanic.LifecycleService;
import org.grimjo.macrocore.game.logic.mechanic.OrderService;
import org.grimjo.macrocore.game.logic.mechanic.SurvivalService;
import org.grimjo.macrocore.game.logic.mechanic.TownAssemblyService;
import org.grimjo.macrocore.game.logic.planner.TaskPlanner;
import org.grimjo.macrocore.game.logic.policy.SurvivalPolicy;
import org.grimjo.macrocore.game.processor.brain.BrainProcessor;
import org.grimjo.macrocore.game.processor.decay.DecayProcessor;
import org.grimjo.macrocore.game.processor.settlement.SettlementStateProcessor;
import org.grimjo.macrocore.game.processor.survival.SurvivalProcessor;
import org.grimjo.macrocore.game.processor.task.TaskExecutionProcessor;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.ContentVersionRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.NpcRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.RoomRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.WorldSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.service.SnapshotPersistenceService;
import org.grimjo.macrocore.infrastructure.persistence.service.StaticPersistenceService;
import org.grimjo.macrocore.infrastructure.state.SimulationTicker;
import org.grimjo.macrocore.infrastructure.state.StateHolder;
import org.grimjo.macrocore.infrastructure.state.genesis.GenesisService;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.NpcMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.RoomStaticMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  @Bean
  public GameEngine gameEngine(
      SettlementStateProcessor processor,
      SurvivalProcessor survivalProcessor,
      DecayProcessor decayProcessor,
      BrainProcessor brainProcessor,
      TaskExecutionProcessor taskProcessor) {
    return GameEngine.builder()
        .settlementProcessor(processor)
        .survivalProcessor(survivalProcessor)
        .decayProcessor(decayProcessor)
        .brainProcessor(brainProcessor)
        .taskProcessor(taskProcessor)
        .build();
  }

  @Bean
  public SettlementStateProcessor settlementStateProcessor(
      TownAssemblyService assemblyService, OrderService orderService) {

    return SettlementStateProcessor.builder()
        .townAssemblyService(assemblyService)
        .orderService(orderService)
        .build();
  }

  @Bean
  public SurvivalProcessor survivalProcessor() {
    return SurvivalProcessor.builder().build();
  }

  @Bean
  public DecayProcessor decayProcessor() {
    return DecayProcessor.builder().build();
  }

  @Bean
  public BrainProcessor brainProcessor(TaskPlanner taskPlanner) {
    return BrainProcessor.builder().taskPlanner(taskPlanner).build();
  }

  @Bean
  public TaskExecutionProcessor taskProcessor() {
    return TaskExecutionProcessor.builder().build();
  }

  @Bean
  public TaskPlanner taskPlanner() {
    return TaskPlanner.builder().build();
  }

  @Bean
  public SurvivalService survivalService() {
    return SurvivalService.builder().build();
  }

  @Bean
  public SurvivalPolicy survivalPolicy() {
    return SurvivalPolicy.builder().build();
  }

  @Bean
  public TownAssemblyService townAssemblyService() {
    return TownAssemblyService.builder().build();
  }

  @Bean
  public StateHolder inMemoryStateHolder(
      GenesisService genesisService, SnapshotPersistenceService persistenceService) {
    return StateHolder.builder()
        .genesisService(genesisService)
        .persistenceService(persistenceService)
        .build();
  }

  @Bean
  public SimulationTicker simulationTicker(GameEngine gameEngine, StateHolder stateHolder) {
    return SimulationTicker.builder().gameEngine(gameEngine).stateHolder(stateHolder).build();
  }

  @Bean
  public LifecycleService lifecycleService() {
    return LifecycleService.builder().build();
  }

  @Bean
  public OrderService orderService() {
    return OrderService.builder().build();
  }

  @Bean
  public StaticPersistenceService ContentLoader(
      ContentVersionRepository versionRepository,
      RoomRepository roomRepository,
      NpcRepository templateRepository,
      ObjectMapper objectMapper) {
    return StaticPersistenceService.builder()
        .versionRepository(versionRepository)
        .roomRepository(roomRepository)
        .templateRepository(templateRepository)
        .objectMapper(objectMapper)
        .build();
  }

  @Bean
  public GenesisService genesisService(
      ObjectMapper objectMapper, RoomStaticMapper roomStaticMapper, NpcMapper npcMapper) {
    return GenesisService.builder()
        .objectMapper(objectMapper)
        .roomMapper(roomStaticMapper)
        .npcMapper(npcMapper)
        .build();
  }

  @Bean
  public SnapshotPersistenceService worldStatePersistenceService(
      WorldSnapshotRepository snapshotRepository) {
    return SnapshotPersistenceService.builder().snapshotRepository(snapshotRepository).build();
  }

  @Bean
  public RoomStaticMapper roomMapper() {
    return RoomStaticMapper.builder().build();
  }

  @Bean
  public NpcMapper npcMapper() {
    return NpcMapper.builder().build();
  }
}
