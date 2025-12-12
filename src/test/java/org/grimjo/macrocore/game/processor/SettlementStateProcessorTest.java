package org.grimjo.macrocore.game.processor;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.grimjo.macrocore.game.logic.mechanic.LifecycleServiceResult;
import org.grimjo.macrocore.game.logic.mechanic.LifecycleService;
import org.grimjo.macrocore.game.logic.mechanic.SurvivalService;
import org.grimjo.macrocore.game.logic.mechanic.SurvivalServiceResult;
import org.grimjo.macrocore.game.logic.mechanic.TownAssemblyService;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettlementStateProcessorTest {
  @Mock private TownAssemblyService assemblyService;
  @Mock private SurvivalService survivalService;
  @Mock private LifecycleService lifecycleService;
  @Mock private Map<Long, List<Object>> policiesConfig;

  @InjectMocks private SettlementStateProcessor processor;

  @Test
  void processSmallSettlementPipeline() {
    // GIVEN
    var settlement = SmallSettlement.builder().id(1L).foodStock(100L).build();

    when(survivalService.processDailySurvival(anyList(), anyLong()))
        .thenReturn(
            SurvivalServiceResult.builder().survivors(emptyList()).remainingFood(90L).build());
    when(lifecycleService.processLifecycle(anyList()))
        .thenReturn(
            LifecycleServiceResult.builder().survivors(emptyList()).corpses(emptyList()).build());
    when(assemblyService.holdMeeting(any(), any())).thenReturn(emptyList());

    // WHEN
    var result = processor.process(settlement);

    // THEN
    assertThat(result).isInstanceOf(SmallSettlement.class);
    var smallResult = (SmallSettlement) result;

    assertThat(smallResult.getFoodStock()).isEqualTo(90L);

    verify(survivalService).processDailySurvival(anyList(), eq(100L));
    verify(assemblyService).holdMeeting(any(SettlementProcessingContext.class), any());
  }
}
