package org.grimjo.macrocore.game.logic.mechanic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.model.order.OrderType;
import org.grimjo.macrocore.game.model.order.SimpleOrder;
import org.grimjo.macrocore.game.model.politic.Decree;
import org.grimjo.macrocore.game.model.politic.DecreeType;

@Builder
@RequiredArgsConstructor
public class OrderService {
  private static final int MAX_ORDERS_PER_TYPE = 5;

  public List<SimpleOrder> generateOrders(List<SimpleOrder> currentOrders, List<Decree> decrees) {
    List<SimpleOrder> nextOrders = new ArrayList<>(currentOrders);

    Map<OrderType, Long> activeOrdersCount =
        nextOrders.stream()
            .collect(Collectors.groupingBy(SimpleOrder::getType, Collectors.counting()));

    for (Decree decree : decrees) {
      generateOrdersForDecree(decree, nextOrders, activeOrdersCount);
    }

    return nextOrders;
  }

  private void generateOrdersForDecree(
      Decree decree, List<SimpleOrder> orders, Map<OrderType, Long> counts) {
    DecreeType type = decree.getType();

    switch (type) {
      case FOOD_SUPPLY -> addOrderIfNeeded(orders, counts, OrderType.COLLECT_FOOD, "Gain food", 5);
      default -> {}
    }
  }

  private void addOrderIfNeeded(
      List<SimpleOrder> orders,
      Map<OrderType, Long> counts,
      OrderType type,
      String desc,
      int priority) {
    long currentCount = counts.getOrDefault(type, 0L);

    if (currentCount < MAX_ORDERS_PER_TYPE) {
      SimpleOrder newOrder =
          SimpleOrder.builder().type(type).description(desc).priority(priority).build();

      orders.add(newOrder);

      counts.put(type, currentCount + 1);
    }
  }
}
