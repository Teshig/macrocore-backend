package org.grimjo.macrocore.game.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import org.grimjo.macrocore.game.model.global.WorldState;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;
import org.grimjo.macrocore.game.processor.brain.BrainProcessor;
import org.grimjo.macrocore.game.processor.brain.BrainProcessorContext;
import org.grimjo.macrocore.game.processor.brain.BrainProcessorResult;
import org.grimjo.macrocore.game.processor.decay.DecayProcessor;
import org.grimjo.macrocore.game.processor.decay.DecayProcessorContext;
import org.grimjo.macrocore.game.processor.decay.DecayProcessorResult;
import org.grimjo.macrocore.game.processor.settlement.SettlementStateProcessor;
import org.grimjo.macrocore.game.processor.survival.SurvivalProcessor;
import org.grimjo.macrocore.game.processor.survival.SurvivalProcessorResult;
import org.grimjo.macrocore.game.processor.task.TaskExecutionProcessor;
import org.grimjo.macrocore.game.processor.task.TaskExecutionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameEngineTest {
  @Mock private SettlementStateProcessor settlementProcessor;
  @Mock private SurvivalProcessor survivalProcessor;
  @Mock private BrainProcessor brainProcessor;
  @Mock private TaskExecutionProcessor taskProcessor;
  @Mock private DecayProcessor decayProcessor;

  @InjectMocks
  private GameEngine gameEngine;

  @BeforeEach
  void setUp() {
    when(survivalProcessor.processAll(any())).thenReturn(
        SurvivalProcessorResult.builder()
            .alive(Collections.emptyMap())
            .dead(Collections.emptyMap())
            .build()
    );

    when(brainProcessor.process(any(BrainProcessorContext.class))).thenReturn(
        BrainProcessorResult.builder()
            .updatedNpcs(Collections.emptyMap())
            .build()
    );

    when(taskProcessor.process(any())).thenReturn(
        TaskExecutionResult.builder()
            .updatedNpcs(Collections.emptyMap())
            .transactions(Collections.emptyList())
            .build()
    );

    when(decayProcessor.process(any(DecayProcessorContext.class))).thenReturn(
        DecayProcessorResult.builder()
            .corpses(Collections.emptyMap())
            .purgeSchedule(Collections.emptySortedMap())
            .build()
    );
  }

  @Test
  void processTick_incrementTickAndProcessAllSettlements() {
    // GIVEN
    var s1 = SmallSettlement.builder().id("1L").build();
    var s2 = SmallSettlement.builder().id("2L").build();

    var currentWorld = WorldState.builder()
        .tick(5L)
        .settlements(Map.of("1L", s1, "2L", s2))
        .population(Collections.emptyMap())
        .corpses(Collections.emptyMap())
        .purgeSchedule(Collections.emptySortedMap())
        .build();

    // Настраиваем поведение для SettlementProcessor (он теперь принимает транзакции вторым аргументом)
    when(settlementProcessor.process(any(SmallSettlement.class), anyList()))
        .thenAnswer(invocation -> {
          SmallSettlement incoming = invocation.getArgument(0);
          // Эмулируем изменение состояния
          if (Objects.equals(incoming.getId(), "1L")) {
            return incoming.toBuilder().foodStock(10L).build();
          }
          return incoming.toBuilder().foodStock(20L).build();
        });

    // WHEN
    var nextWorld = gameEngine.processTick(currentWorld);

    // THEN
    assertThat(nextWorld.getTick()).isEqualTo(6L);

    verify(settlementProcessor, times(2)).process(any(SmallSettlement.class), anyList());

    assertThat(nextWorld.getSettlements()).hasSize(2);
    assertThat((nextWorld.getSettlements().get("1L")).getFoodStock()).isEqualTo(10L);
    assertThat((nextWorld.getSettlements().get("2L")).getFoodStock()).isEqualTo(20L);
  }

  @Test
  void processTick_handleEmptyWorld() {
    // GIVEN
    var currentWorld = WorldState.builder()
        .tick(10L)
        .settlements(Collections.emptyMap())
        .population(Collections.emptyMap())
        .corpses(Collections.emptyMap())
        .purgeSchedule(Collections.emptySortedMap())
        .build();

    // WHEN
    var nextWorld = gameEngine.processTick(currentWorld);

    // THEN
    assertThat(nextWorld.getTick()).isEqualTo(11L);
    assertThat(nextWorld.getSettlements()).isEmpty();

    verify(survivalProcessor).processAll(any());
    verify(settlementProcessor, times(0)).process(any(), anyList());
  }
}
