package org.grimjo.macrocore.infrastructure.persistence.json.source;

import java.util.List;
import lombok.Data;

@Data
public class ZoneSourceJson {
  private Long id;
  private String name;
  private String description;

  private List<RoomSourceJson> rooms;
}
