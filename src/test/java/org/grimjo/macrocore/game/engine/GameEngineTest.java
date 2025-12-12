package org.grimjo.macrocore.game.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;
import org.grimjo.macrocore.game.model.global.WorldState;
import org.grimjo.macrocore.game.model.settlement.Settlement;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;
import org.grimjo.macrocore.game.processor.SettlementStateProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameEngineTest {
  @Mock
  private SettlementStateProcessor settlementProcessor;

  @InjectMocks
  private GameEngine gameEngine;

  @Test
  void processTick_ShouldIncrementTickAndProcessAllSettlements() {
    // GIVEN
    var s1 = SmallSettlement.builder().id(1L).build();
    var s2 = SmallSettlement.builder().id(2L).build();

    var currentWorld = WorldState.builder()
        .tick(5L)
        .settlements(Map.of(1L, s1, 2L, s2))
        .build();

    when(settlementProcessor.process(any(Settlement.class)))
        .thenAnswer(invocation -> {
          Settlement incoming = invocation.getArgument(0);
          if (incoming.getId() == 1L) {
            return SmallSettlement.builder().id(1L).foodStock(10L).build();
          }
          return SmallSettlement.builder().id(2L).foodStock(20L).build();
        });

    // WHEN
    var nextWorld = gameEngine.processTick(currentWorld);

    // THEN
    assertThat(nextWorld.getTick()).isEqualTo(6L);
    verify(settlementProcessor, times(2)).process(any(Settlement.class));
    assertThat(nextWorld.getSettlements()).hasSize(2);
    assertThat(nextWorld.getSettlements().get(1L).getFoodStock()).isEqualTo(10L);
    assertThat(nextWorld.getSettlements().get(2L).getFoodStock()).isEqualTo(20L);
  }

  @Test
  void processTick_ShouldHandleEmptyWorld() {
    // GIVEN
    var currentWorld = WorldState.builder()
        .tick(10L)
        .settlements(Collections.emptyMap())
        .build();

    // WHEN
    var nextWorld = gameEngine.processTick(currentWorld);

    // THEN
    assertThat(nextWorld.getTick()).isEqualTo(11L);
    assertThat(nextWorld.getSettlements()).isEmpty();
    verify(settlementProcessor, times(0)).process(any());
  }
}
