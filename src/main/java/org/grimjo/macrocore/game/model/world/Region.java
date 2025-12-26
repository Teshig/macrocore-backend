package org.grimjo.macrocore.game.model.world;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Region {
  String id;
  String name;
  String biomeType;
  int dangerLevel;
}
