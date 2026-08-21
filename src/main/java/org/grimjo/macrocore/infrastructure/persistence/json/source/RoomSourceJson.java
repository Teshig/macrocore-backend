package org.grimjo.macrocore.infrastructure.persistence.json.source;

import java.util.Map;
import lombok.Data;
import org.grimjo.macrocore.infrastructure.persistence.json.ExitTargetJson;

@Data
public class RoomSourceJson {
  private Long id;
  private String name;
  private String description;

  private Map<org.grimjo.macrocore.game.domain.world.Direction, ExitTargetJson> exits;
}
