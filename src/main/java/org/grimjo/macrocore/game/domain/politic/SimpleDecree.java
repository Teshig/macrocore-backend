package org.grimjo.macrocore.game.domain.politic;

import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SimpleDecree implements Decree {
  int priority;
  DecreeType type;
  Map<String, String> parameters;
}
