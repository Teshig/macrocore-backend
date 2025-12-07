package org.grimjo.macrocore.game.model;

import java.util.Collections;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class WorldState {
  @Builder.Default long tick = 0;

  @Builder.Default Map<Long, Settlement> settlements = Collections.emptyMap();

  public static WorldState initial(Map<Long, Settlement> settlements) {
    return WorldState.builder()
        .tick(0)
        .settlements(settlements)
        .build();
  }
}
