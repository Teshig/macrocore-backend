package org.grimjo.macrocore.infrastructure.configuration;

import java.util.List;
import java.util.Map;
import org.grimjo.macrocore.game.engine.GameEngine;
import org.grimjo.macrocore.game.logic.SurvivalPolicy;
import org.grimjo.macrocore.game.logic.TownAssemblyService;
import org.grimjo.macrocore.game.model.Policy;
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
    Map<Long, List<Policy>> registry = Map.of(1L, List.of(survivalPolicy));
    return new TownAssemblyService(registry);
  }

  @Bean
  public GameEngine gameEngine(TownAssemblyService assemblyService) {
    return new GameEngine(assemblyService);
  }

  @Bean
  public InMemoryStateHolder inMemoryStateHolder() {
    return new InMemoryStateHolder();
  }

  @Bean
  public SimulationTicker simulationTicker(GameEngine gameEngine, InMemoryStateHolder stateHolder) {
    return new SimulationTicker(gameEngine, stateHolder);
  }
}
