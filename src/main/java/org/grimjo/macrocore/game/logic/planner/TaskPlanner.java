package org.grimjo.macrocore.game.logic.planner;

import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.domain.action.Action;
import org.grimjo.macrocore.game.domain.action.ActionType;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.order.SimpleOrder;

@Builder
@RequiredArgsConstructor
public class TaskPlanner {
  private static final Long TOWN_SQUARE_ID = 1L;
  private static final Long FOREST_ID = 2L;

  public List<Action> planMoveToSquare(NpcBase npc) {
    if (TOWN_SQUARE_ID.equals(npc.getRoomId().getValue())) {
      return List.of();
    }

    return List.of(Action.builder()
        .type(ActionType.MOVE)
        .targetId(String.valueOf(TOWN_SQUARE_ID))
        .duration(10) // Идти 10 тиков
        .build());
  }

  public List<Action> planOrder(SimpleOrder order, NpcBase npc) {
    return switch (order.getType()) {
      case COLLECT_FOOD -> planForage(npc);
      case IDLE -> List.of(Action.builder().type(ActionType.WAIT).duration(20).build());
      default -> List.of(Action.builder().type(ActionType.WAIT).duration(5).build());
    };
  }

  private List<Action> planForage(NpcBase npc) {
    return List.of(
        Action.builder().type(ActionType.MOVE).targetId(String.valueOf(FOREST_ID)).duration(3).build(),
        Action.builder().type(ActionType.WAIT).duration(5).build(), // Сбор
        Action.builder().type(ActionType.MOVE).targetId(String.valueOf(TOWN_SQUARE_ID)).duration(3).build(),

        Action.builder()
            .type(ActionType.COMPLETE_ORDER)
            .targetId(String.valueOf(npc.getSettlementId()))
            .duration(1)
            .build()
    );
  }
}
