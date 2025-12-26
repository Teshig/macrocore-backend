package org.grimjo.macrocore.game.processor.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.model.action.Action;
import org.grimjo.macrocore.game.model.action.ActionType;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.settlement.SettlementTransaction;
import org.grimjo.macrocore.game.model.world.RoomId;

@Builder
@RequiredArgsConstructor
public class TaskExecutionProcessor {
  public TaskExecutionResult process(Map<String, NpcBase> population) {
    Map<String, NpcBase> updatedNpcs = new HashMap<>();
    List<SettlementTransaction> transactions = new ArrayList<>();

    for (NpcBase npc : population.values()) {
      if (npc.getActionQueue().isEmpty()) {
        continue;
      }

      Action currentAction = npc.getActionQueue().get(0);
      int remainingDuration = currentAction.getDuration() - 1;

      NpcBase.NpcBaseBuilder npcBuilder = npc.toBuilder();
      List<Action> nextQueue = new ArrayList<>(npc.getActionQueue());

      if (remainingDuration <= 0) {

        if (currentAction.getType() == ActionType.COMPLETE_ORDER) {
          int reward = 10;

          npcBuilder.money(npc.getMoney() + reward);
          npcBuilder.currentOrderId(null);

          transactions.add(SettlementTransaction.builder()
              .settlementId(npc.getSettlementId())
              .orderId(npc.getCurrentOrderId())
              .foodAdded(50)
              .build());
        } else {
          applyActionSideEffects(npcBuilder, currentAction);
        }
        nextQueue.remove(0);
      } else {
        Action updatedAction = currentAction.toBuilder().duration(remainingDuration).build();
        nextQueue.set(0, updatedAction);
      }

      updatedNpcs.put(npc.getId(), npcBuilder.actionQueue(nextQueue).build());
    }

    return TaskExecutionResult.builder()
        .updatedNpcs(updatedNpcs)
        .transactions(Collections.unmodifiableList(transactions))
        .build();
  }

  private void applyActionSideEffects(NpcBase.NpcBaseBuilder npc, Action action) {
    switch (action.getType()) {
      case MOVE -> {
        if (action.getTargetId() != null) {
          npc.roomId(RoomId.of(action.getTargetId()));
        }
      }
      case WAIT -> {
      }
      case PICKUP -> {
      }
    }
  }
}
