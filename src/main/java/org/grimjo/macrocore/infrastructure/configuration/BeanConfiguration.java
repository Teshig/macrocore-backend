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
public class BeanConfiguration {

  @Bean
  public SurvivalPolicy survivalPolicy() {
    return new SurvivalPolicy();
  }

  @Bean
  public TownAssemblyService townAssemblyService(SurvivalPolicy survivalPolicy) {
    return new TownAssemblyService();
  }

  @Bean
  public SettlementStateProcessor settlementStateProcessor(
      TownAssemblyService assemblyService,
      SurvivalService survivalService,
      LifecycleService lifecycleService,
      SurvivalPolicy survivalPolicy) {
    Map<Long, List<Policy>> registry = Map.of(0L, List.of(survivalPolicy));
    return new SettlementStateProcessor(
        assemblyService, survivalService, lifecycleService, registry);
  }

  @Bean
  public GameEngine gameEngine(SettlementStateProcessor processor) {
    return new GameEngine(processor);
  }

  @Bean
  public InMemoryStateHolder inMemoryStateHolder() {
    return new InMemoryStateHolder();
  }

  @Bean
  public SimulationTicker simulationTicker(GameEngine gameEngine, InMemoryStateHolder stateHolder) {
    return new SimulationTicker(gameEngine, stateHolder);
  }

  @Bean
  public SurvivalService survivalService() {
    return new SurvivalService();
  }

  @Bean
  public LifecycleService lifecycleService() {
    return new LifecycleService();
  }
}
