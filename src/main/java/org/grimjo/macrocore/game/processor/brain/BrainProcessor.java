package org.grimjo.macrocore.game.processor.brain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.logic.planner.TaskPlanner;
import org.grimjo.macrocore.game.model.action.Action;
import org.grimjo.macrocore.game.model.action.ActionType;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.order.SimpleOrder;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;

@Builder
@RequiredArgsConstructor
public class BrainProcessor {
  private final TaskPlanner taskPlanner;

  public BrainProcessorResult process(BrainProcessorContext context) {

    Map<String, NpcBase> updatedNpcs = new HashMap<>();

    for (NpcBase npc : context.getPopulation().values()) {
      if (!npc.getActionQueue().isEmpty()) {
        continue;
      }
      updatedNpcs.put(npc.getId(), think(npc, context));
    }

    return BrainProcessorResult.builder()
        .updatedNpcs(updatedNpcs)
        .build();
  }

  private NpcBase think(NpcBase npc, BrainProcessorContext context) {
    if (!npc.getActionQueue().isEmpty()) {
      return npc;
    }

    SmallSettlement settlement = context.getSettlements().get(npc.getSettlementId());

    if (npc.getCurrentOrderId() != null) {
      Optional<SimpleOrder> orderOpt = findOrderById(settlement, npc.getCurrentOrderId());

      if (orderOpt.isPresent()) {
        List<Action> newPlan = taskPlanner.planOrder(orderOpt.get(), npc);
        return npc.toBuilder().actionQueue(newPlan).build();
      } else {
        return npc.toBuilder().currentOrderId(null).build();
      }
    }

    boolean atMeetingPoint = "village_square".equals(npc.getRoomId().getValue());

    if (!atMeetingPoint) {
      List<Action> goToSquare = taskPlanner.planMoveToSquare(npc);
      return npc.toBuilder().actionQueue(goToSquare).build();
    } else {
      Optional<SimpleOrder> bestOrder = findBestOrder(settlement.getOrders());

      if (bestOrder.isPresent()) {
        SimpleOrder order = bestOrder.get();
        List<Action> plan = taskPlanner.planOrder(order, npc);

        return npc.toBuilder()
            .currentOrderId(order.getId())
            .actionQueue(plan)
            .build();
      } else {
        return npc.toBuilder()
            .actionQueue(List.of(Action.builder().type(ActionType.WAIT).duration(2).build()))
            .build();
      }
    }
  }

  private Optional<SimpleOrder> findOrderById(SmallSettlement settlement, String orderId) {
    return settlement.getOrders().stream().filter(o -> o.getId().equals(orderId)).findFirst();
  }

  private Optional<SimpleOrder> findBestOrder(List<SimpleOrder> orders) {
    return orders.stream().max(Comparator.comparingInt(SimpleOrder::getPriority));
  }
}