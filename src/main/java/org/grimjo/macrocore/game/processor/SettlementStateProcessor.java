package org.grimjo.macrocore.game.processor;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.logic.mechanic.LifecycleService;
import org.grimjo.macrocore.game.logic.mechanic.SurvivalService;
import org.grimjo.macrocore.game.logic.mechanic.TownAssemblyService;
import org.grimjo.macrocore.game.model.politic.Policy;
import org.grimjo.macrocore.game.model.settlement.Settlement;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;

@Builder
@RequiredArgsConstructor
public class SettlementStateProcessor {
  private final TownAssemblyService assemblyService;
  private final SurvivalService survivalService;
  private final LifecycleService lifecycleService;

  private final Map<Long, List<Policy>> settlementPoliciesConfig;

  public Settlement process(Settlement settlement) {
    return switch (settlement) {
      case SmallSettlement small -> processSmallSettlement(small);
    };
  }

  private SmallSettlement processSmallSettlement(SmallSettlement small) {
    var survivalResult =
        survivalService.processDailySurvival(small.getSettlers(), small.getFoodStock());

    var lifecycleResult = lifecycleService.processLifecycle(survivalResult.getSurvivors());

    var context =
        SettlementProcessingContext.builder()
            .foodStock(small.getFoodStock())
            .population(small.getSettlers())
            .decrees(small.getDecrees())
            .build();
    var policies = settlementPoliciesConfig.getOrDefault(small.getId(), List.of());
    var decrees = assemblyService.holdMeeting(context, policies);

    return small.toBuilder()
        .foodStock(survivalResult.getRemainingFood())
        .clearSettlers()
        .settlers(lifecycleResult.getSurvivors())
        .clearDecrees()
        .decrees(decrees)
        .build();
  }
}
