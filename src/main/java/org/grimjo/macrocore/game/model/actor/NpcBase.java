package org.grimjo.macrocore.game.model.actor;

import lombok.Builder;
import lombok.Value;
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
  @Builder.Default NpcStatus status = NpcStatus.ALIVE;

  public int getConsumption() {
    return DAILY_CONSUMPTION;
  }

  public boolean isDead() {
    return NpcStatus.DEAD.equals(status);
  }
}
