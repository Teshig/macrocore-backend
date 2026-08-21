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
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.PlayerRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.RoomRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.WorldSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.service.SnapshotPersistenceService;
import org.grimjo.macrocore.infrastructure.persistence.service.StaticPersistenceService;
import org.grimjo.macrocore.infrastructure.state.SimulationTicker;
import org.grimjo.macrocore.infrastructure.state.StateHolder;
import org.grimjo.macrocore.infrastructure.state.partition.PartitionedStateRegistry;
import org.grimjo.macrocore.infrastructure.state.genesis.GenesisService;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.NpcStaticMapper;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.ZoneRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.SettlementRepository;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.ZoneContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.RoomContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.NpcContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.ZoneContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.NpcContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.PlayerContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.PlayerStaticMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.SettlementContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.RoomContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.StaticDomainMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.ZoneStaticMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.RoomStaticMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.SettlementStaticMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot.NpcSnapshotMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot.OrderSnapshotMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot.PlayerSnapshotMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot.RoomSnapshotMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot.SettlementSnapshotMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot.ZoneSnapshotMapper;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.ZoneSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.RoomSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.NpcSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.PlayerSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.SettlementSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.OrderSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot.WorldSnapshotMapper;
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
      TaskExecutionProcessor taskProcessor,
      PartitionedStateRegistry partitionedStateRegistry) {
    return GameEngine.builder()
        .settlementProcessor(processor)
        .survivalProcessor(survivalProcessor)
        .decayProcessor(decayProcessor)
        .brainProcessor(brainProcessor)
        .taskProcessor(taskProcessor)
        .partitionedRegistry(partitionedStateRegistry)
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
      GenesisService genesisService, SnapshotPersistenceService persistenceService, PartitionedStateRegistry partitionedStateRegistry) {
    return StateHolder.builder()
        .genesisService(genesisService)
        .persistenceService(persistenceService)
        .partitionedStateRegistry(partitionedStateRegistry)
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
      ZoneRepository zoneRepository,
      RoomRepository roomRepository,
      NpcRepository npcRepository,
      PlayerRepository playerRepository,
      SettlementRepository settlementRepository,
      ZoneContentMapper zoneMapper,
      RoomContentMapper roomMapper,
      NpcContentMapper npcMapper,
      PlayerContentMapper playerMapper,
      SettlementContentMapper settlementMapper,
      ObjectMapper objectMapper) {
    return StaticPersistenceService.builder()
        .versionRepository(versionRepository)
        .zoneRepository(zoneRepository)
        .roomRepository(roomRepository)
        .npcRepository(npcRepository)
        .playerRepository(playerRepository)
        .settlementRepository(settlementRepository)
        .zoneMapper(zoneMapper)
        .roomMapper(roomMapper)
        .npcMapper(npcMapper)
        .playerMapper(playerMapper)
        .settlementMapper(settlementMapper)
        .objectMapper(objectMapper)
        .build();
  }

  @Bean
  public GenesisService genesisService(
      ZoneRepository zoneRepository,
      RoomRepository roomRepository,
      NpcRepository npcRepository,
      PlayerRepository playerRepository,
      SettlementRepository settlementRepository,
      StaticDomainMapper staticDomainMapper) {
    return GenesisService.builder()
        .zoneRepository(zoneRepository)
        .roomRepository(roomRepository)
        .npcRepository(npcRepository)
        .playerRepository(playerRepository)
        .settlementRepository(settlementRepository)
        .domainMapper(staticDomainMapper)
        .build();
  }

  @Bean
  public SnapshotPersistenceService worldStatePersistenceService(
      WorldSnapshotRepository snapshotRepository,
      ZoneSnapshotRepository zoneRepo,
      RoomSnapshotRepository roomRepo,
      NpcSnapshotRepository npcRepo,
      PlayerSnapshotRepository playerRepo,
      SettlementSnapshotRepository settlementRepo,
      OrderSnapshotRepository orderRepo,
      WorldSnapshotMapper worldSnapshotMapper) {
    return SnapshotPersistenceService.builder()
        .worldRepo(snapshotRepository)
        .zoneRepo(zoneRepo)
        .roomRepo(roomRepo)
        .npcRepo(npcRepo)
        .playerRepo(playerRepo)
        .settlementRepo(settlementRepo)
        .orderRepo(orderRepo)
        .worldSnapshotMapper(worldSnapshotMapper)
        .build();
  }

  @Bean
  public ZoneContentMapper zoneContentMapper() {
    return new ZoneContentMapper();
  }

  @Bean
  public NpcContentMapper npcContentMapper() {
    return new NpcContentMapper();
  }

  @Bean
  public PlayerContentMapper playerContentMapper() {
    return new PlayerContentMapper();
  }

  @Bean
  public SettlementContentMapper settlementContentMapper() {
    return new SettlementContentMapper();
  }

  @Bean
  public RoomContentMapper roomContentMapper() {
    return new RoomContentMapper();
  }

  @Bean
  public ZoneStaticMapper zoneStaticMapper() {
    return new ZoneStaticMapper();
  }

  @Bean
  public RoomStaticMapper roomStaticMapper() {
    return RoomStaticMapper.builder().build();
  }

  @Bean
  public SettlementStaticMapper settlementStaticMapper() {
    return SettlementStaticMapper.builder().build();
  }

  @Bean
  public PlayerStaticMapper playerStaticMapper() {
    return PlayerStaticMapper.builder().build();
  }

  @Bean
  public StaticDomainMapper staticDomainMapper(
      ZoneStaticMapper zoneMapper,
      RoomStaticMapper roomMapper,
      NpcStaticMapper npcMapper,
      PlayerStaticMapper playerMapper,
      SettlementStaticMapper settlementMapper) {
    return new StaticDomainMapper(zoneMapper, roomMapper, npcMapper, settlementMapper, playerMapper);
  }

  @Bean
  public NpcStaticMapper npcMapper() {
    return NpcStaticMapper.builder().build();
  }

  @Bean
  public NpcSnapshotMapper npcSnapshotMapper() {
    return new NpcSnapshotMapper();
  }

  @Bean
  public RoomSnapshotMapper roomSnapshotMapper() {
    return new RoomSnapshotMapper();
  }

  @Bean
  public PlayerSnapshotMapper playerSnapshotMapper() {
    return new PlayerSnapshotMapper();
  }

  @Bean
  public SettlementSnapshotMapper settlementSnapshotMapper() {
    return new SettlementSnapshotMapper();
  }

  @Bean
  public OrderSnapshotMapper orderSnapshotMapper() {
    return new OrderSnapshotMapper();
  }

  @Bean
  public ZoneSnapshotMapper zoneSnapshotMapper() {
    return new ZoneSnapshotMapper();
  }

  @Bean
  public WorldSnapshotMapper worldSnapshotMapper(
      NpcSnapshotMapper npcMapper,
      RoomSnapshotMapper roomMapper,
      SettlementSnapshotMapper settlementMapper,
      OrderSnapshotMapper orderMapper,
      ZoneSnapshotMapper zoneMapper,
      PlayerSnapshotMapper playerMapper) {
    return new WorldSnapshotMapper(npcMapper, roomMapper, settlementMapper, orderMapper, zoneMapper, playerMapper);
  }
}
