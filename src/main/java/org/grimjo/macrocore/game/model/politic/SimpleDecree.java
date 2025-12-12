package org.grimjo.macrocore.game.model.politic;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SimpleDecree implements Decree {
  int priority;
  DecreeType type;
}
