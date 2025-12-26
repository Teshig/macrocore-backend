package org.grimjo.macrocore.game.model.actor;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.model.action.Action;
import org.grimjo.macrocore.game.model.world.RoomId;

@Value
@Builder(toBuilder = true)
public class NpcBase {
  public static final int DAILY_CONSUMPTION = 1;

  String id;
  String name;
  String description;
  RoomId roomId;
  int health;
  int hunger;
  int money;
  @Builder.Default NpcStatus status = NpcStatus.ALIVE;

  @Builder.Default
  List<Action> actionQueue = List.of();

  String settlementId;
  String currentOrderId;

  public int getConsumption() {
    return DAILY_CONSUMPTION;
  }

  public boolean isDead() {
    return NpcStatus.DEAD.equals(status);
  }
}
