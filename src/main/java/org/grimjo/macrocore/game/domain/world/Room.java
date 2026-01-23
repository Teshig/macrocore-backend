package org.grimjo.macrocore.game.domain.world;

import java.util.Map;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.grimjo.macrocore.game.domain.world.Zone.ZoneId;

@Value
@Builder
public class Room {
  RoomId id;

  ZoneId zoneId;
  String name;
  String description;

  @Singular Map<Direction, ExitTarget> exits;

  @Value(staticConstructor = "of")
  public static class RoomId {
    Long value;
  }

  @Value(staticConstructor = "of")
  public static class ExitTarget {
    Long zoneId;
    Long roomId;
  }
}
