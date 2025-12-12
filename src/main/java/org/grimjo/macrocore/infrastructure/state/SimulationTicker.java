package org.grimjo.macrocore.infrastructure.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.engine.GameEngine;
import org.grimjo.macrocore.game.model.global.WorldState;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
public class SimulationTicker {
  private final GameEngine gameEngine;
  private final InMemoryStateHolder stateHolder;

  @Scheduled(fixedRate = 3000)
  public void tick() {
    WorldState current = stateHolder.get();
    if (current == null) {
      log.warn("World not loaded yet, skipping tick.");
      return;
    }

    try {
      WorldState nextWorld = gameEngine.processTick(current);

      stateHolder.update(nextWorld);
      log.debug(
          "Tick {} processed. Settlements: {}",
          nextWorld.getTick(),
          nextWorld.getSettlements());
    } catch (Exception ex) {
      log.error("Error during game tick processing", ex);
    }
  }
}
