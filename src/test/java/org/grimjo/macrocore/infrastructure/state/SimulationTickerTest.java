package org.grimjo.macrocore.infrastructure.state;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.grimjo.macrocore.game.engine.GameEngine;
import org.grimjo.macrocore.game.model.global.WorldState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimulationTickerTest {
  @Mock
  private GameEngine gameEngine;

  @Mock
  private InMemoryStateHolder stateHolder;

  @InjectMocks
  private SimulationTicker simulationTicker;

  @Test
  void tick_skipProcessingWhenWorldIsNotLoaded() {
    // GIVEN
    when(stateHolder.get()).thenReturn(null);

    // WHEN
    simulationTicker.tick();

    // THEN
    verify(gameEngine, never()).processTick(any());
    verify(stateHolder, never()).update(any());
  }

  @Test
  void tick_processWorldAndSaveNewState() {
    // GIVEN
    var currentWorld = WorldState.builder().tick(1L).build();
    var nextWorld = WorldState.builder().tick(2L).build();

    when(stateHolder.get()).thenReturn(currentWorld);
    when(gameEngine.processTick(currentWorld)).thenReturn(nextWorld);

    // WHEN
    simulationTicker.tick();

    // THEN
    verify(gameEngine).processTick(currentWorld);
    verify(stateHolder).update(nextWorld);
  }

  @Test
  void tick_handleExceptionDuringGameEngineProcessing() {
    // GIVEN
    var currentWorld = WorldState.builder().tick(1L).build();

    when(stateHolder.get()).thenReturn(currentWorld);
    doThrow(new RuntimeException("Simulated Engine Error"))
        .when(gameEngine)
        .processTick(any());

    // WHEN
    simulationTicker.tick();

    // THEN
    verify(gameEngine).processTick(currentWorld);
    verify(stateHolder, never()).update(any());
  }
}
