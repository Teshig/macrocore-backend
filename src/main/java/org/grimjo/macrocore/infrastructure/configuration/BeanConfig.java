package org.grimjo.macrocore.infrastructure.configuration;

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
import org.grimjo.macrocore.infrastructure.state.InMemoryStateHolder;
import org.grimjo.macrocore.infrastructure.state.SimulationTicker;
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
      TownAssemblyService assemblyService,
      SurvivalService survivalService,
      LifecycleService lifecycleService,
      OrderService orderService) {

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
  public InMemoryStateHolder inMemoryStateHolder(SurvivalPolicy survivalPolicy) {
    return InMemoryStateHolder.builder().survivalPolicy(survivalPolicy).build();
  }

  @Bean
  public SimulationTicker simulationTicker(GameEngine gameEngine, InMemoryStateHolder stateHolder) {
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
}
