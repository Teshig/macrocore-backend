package org.grimjo.macrocore.game.model.global;

import java.util.Collections;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.model.settlement.Settlement;

@Value
@Builder(toBuilder = true)
public class WorldState {
  long tick;

  @Builder.Default Map<Long, Settlement> settlements = Collections.emptyMap();
}
