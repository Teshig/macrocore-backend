package org.grimjo.macrocore.game.domain.world;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Zone {
  ZoneId id;

  String name;
  String description;
  int level;

  @Value(staticConstructor = "of")
  public static class ZoneId {
    Long value;
  }
}
