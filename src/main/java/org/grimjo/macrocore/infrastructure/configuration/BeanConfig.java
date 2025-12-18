package org.grimjo.macrocore.infrastructure.configuration;

import java.util.List;
import java.util.Map;
import org.grimjo.macrocore.game.engine.GameEngine;
import org.grimjo.macrocore.game.logic.mechanic.OrderService;
import org.grimjo.macrocore.game.processor.decay.DecayProcessor;
import org.grimjo.macrocore.game.processor.settlement.SettlementStateProcessor;
import org.grimjo.macrocore.game.logic.mechanic.LifecycleService;
import org.grimjo.macrocore.game.logic.mechanic.SurvivalService;
import org.grimjo.macrocore.game.logic.policy.SurvivalPolicy;
import org.grimjo.macrocore.game.logic.mechanic.TownAssemblyService;
import org.grimjo.macrocore.game.model.politic.Policy;
import org.grimjo.macrocore.game.processor.survival.SurvivalProcessor;
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
      DecayProcessor decayProcessor) {
    return GameEngine.builder()
        .settlementProcessor(processor)
        .survivalProcessor(survivalProcessor)
        .decayProcessor(decayProcessor)
        .build();
  }

  @Bean
  public SettlementStateProcessor settlementStateProcessor(
      TownAssemblyService assemblyService,
      SurvivalService survivalService,
      LifecycleService lifecycleService,
      SurvivalPolicy survivalPolicy,
      OrderService orderService) {
    Map<Long, List<Policy>> registry = Map.of(0L, List.of(survivalPolicy));
    return SettlementStateProcessor.builder()
        .townAssemblyService(assemblyService)
        .settlementPoliciesConfig(registry)
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
  public InMemoryStateHolder inMemoryStateHolder() {
    return InMemoryStateHolder.builder().build();
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
