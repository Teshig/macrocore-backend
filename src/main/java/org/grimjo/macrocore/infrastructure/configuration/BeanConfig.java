package org.grimjo.macrocore.infrastructure.configuration;

import java.util.List;
import java.util.Map;
import org.grimjo.macrocore.game.engine.GameEngine;
import org.grimjo.macrocore.game.processor.SettlementStateProcessor;
import org.grimjo.macrocore.game.logic.mechanic.LifecycleService;
import org.grimjo.macrocore.game.logic.mechanic.SurvivalService;
import org.grimjo.macrocore.game.logic.policy.SurvivalPolicy;
import org.grimjo.macrocore.game.logic.mechanic.TownAssemblyService;
import org.grimjo.macrocore.game.model.politic.Policy;
import org.grimjo.macrocore.infrastructure.state.InMemoryStateHolder;
import org.grimjo.macrocore.infrastructure.state.SimulationTicker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  @Bean
  public SurvivalPolicy survivalPolicy() {
    return SurvivalPolicy.builder().build();
  }

  @Bean
  public TownAssemblyService townAssemblyService() {
    return TownAssemblyService.builder().build();
  }

  @Bean
  public SettlementStateProcessor settlementStateProcessor(
      TownAssemblyService assemblyService,
      SurvivalService survivalService,
      LifecycleService lifecycleService,
      SurvivalPolicy survivalPolicy) {
    Map<Long, List<Policy>> registry = Map.of(0L, List.of(survivalPolicy));
    return SettlementStateProcessor.builder()
        .assemblyService(assemblyService)
        .survivalService(survivalService)
        .lifecycleService(lifecycleService)
        .settlementPoliciesConfig(registry)
        .build();
  }

  @Bean
  public GameEngine gameEngine(SettlementStateProcessor processor) {
    return GameEngine.builder().settlementProcessor(processor).build();
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
  public SurvivalService survivalService() {
    return SurvivalService.builder().build();
  }

  @Bean
  public LifecycleService lifecycleService() {
    return LifecycleService.builder().build();
  }
}
