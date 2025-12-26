package org.grimjo.macrocore.game.processor.settlement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.logic.mechanic.OrderService;
import org.grimjo.macrocore.game.logic.mechanic.TownAssemblyService;
import org.grimjo.macrocore.game.model.order.SimpleOrder;
import org.grimjo.macrocore.game.model.settlement.SettlementTransaction;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;
import org.grimjo.macrocore.game.utils.ImmutabilityUtils;

@Builder
@RequiredArgsConstructor
public class SettlementStateProcessor {
  private final TownAssemblyService townAssemblyService;
  private final OrderService orderService;

  public SmallSettlement process(
      SmallSettlement settlement, List<SettlementTransaction> transactions) {
    return processSmallSettlement(settlement, transactions);
  }

  private SmallSettlement processSmallSettlement(
      SmallSettlement settlement, List<SettlementTransaction> transactions) {
    long addedFood = 0;
    List<String> completedOrderIds = new ArrayList<>();

    for (SettlementTransaction tx : transactions) {
      if (Objects.equals(tx.getSettlementId(), settlement.getId())) {
        addedFood += tx.getFoodAdded();
        if (tx.getOrderId() != null) {
          completedOrderIds.add(tx.getOrderId());
        }
      }
    }

    long newFoodStock = settlement.getFoodStock() + addedFood;

    List<SimpleOrder> remainingOrders =
        settlement.getOrders().stream()
            .filter(order -> !completedOrderIds.contains(order.getId()))
            .collect(Collectors.toList());
    var context =
        SettlementProcessingContext.builder()
            .foodStock(newFoodStock)
            .decrees(settlement.getDecrees())
            .build();

    var policies = settlement.getPolicies();

    var newDecrees = townAssemblyService.holdMeeting(context, policies);
    List<SimpleOrder> newOrders = orderService.generateOrders(remainingOrders, newDecrees);

    return settlement.toBuilder()
        .foodStock(newFoodStock)
        .decrees(ImmutabilityUtils.freezeList(newDecrees))
        .orders(ImmutabilityUtils.freezeList(newOrders))
        .build();
  }
}
