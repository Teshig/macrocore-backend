package org.grimjo.macrocore.game.processor.settlement;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.logic.mechanic.OrderService;
import org.grimjo.macrocore.game.logic.mechanic.TownAssemblyService;
import org.grimjo.macrocore.game.model.order.SimpleOrder;
import org.grimjo.macrocore.game.model.politic.Policy;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;
import org.grimjo.macrocore.game.utils.ImmutabilityUtils;

@Builder
@RequiredArgsConstructor
public class SettlementStateProcessor {
  private final TownAssemblyService townAssemblyService;
  private final OrderService orderService;

  private final Map<Long, List<Policy>> settlementPoliciesConfig;

  public SmallSettlement process(SmallSettlement settlement) {
    return processSmallSettlement(settlement);
  }

  private SmallSettlement processSmallSettlement(SmallSettlement settlement) {
    var context = SettlementProcessingContext.builder()
        .foodStock(settlement.getFoodStock())
        .decrees(settlement.getDecrees())
        .build();

    var policies = settlementPoliciesConfig.getOrDefault(settlement.getId(), List.of());

    var newDecrees = townAssemblyService.holdMeeting(context, policies);
    List<SimpleOrder> newOrders = orderService.generateOrders(settlement.getOrders(), newDecrees);

    return settlement.toBuilder()
        .decrees(ImmutabilityUtils.freezeList(newDecrees))
        .orders(ImmutabilityUtils.freezeList(newOrders))
        .build();
  }
}
