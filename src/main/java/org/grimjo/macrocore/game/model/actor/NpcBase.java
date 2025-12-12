package org.grimjo.macrocore.game.model.actor;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class NpcBase {
  public static final long DAILY_CONSUMPTION = 1L;

  long id;
  String name;
  String description;
  int health;
  int hunger;
  @Builder.Default NpcStatus status = NpcStatus.ALIVE;

  public long getConsumption() {
    return DAILY_CONSUMPTION;
  }

  public boolean isDead() {
    return NpcStatus.DEAD.equals(status);
  }
}
