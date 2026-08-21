package org.grimjo.macrocore.game.domain.actor;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.domain.action.Action;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;
import org.grimjo.macrocore.game.domain.world.Zone.ZoneId;

@Value
@Builder(toBuilder = true)
public class Player {

  PlayerId id;

  String name;
  String description;
  ZoneId zoneId;
  RoomId roomId;

  int health;
  int hunger;
  int money;
  
  @Builder.Default NpcStatus status = NpcStatus.ALIVE;

  @Builder.Default List<Action> actionQueue = List.of();

  public boolean isDead() {
    return NpcStatus.DEAD.equals(status);
  }

  @Value(staticConstructor = "of")
  public static class PlayerId {
    Long value;
  }
}
