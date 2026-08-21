package org.grimjo.macrocore.infrastructure.persistence.json.source;

import java.util.Map;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneSourceJson {
  private Long id;
  private String name;
  private String description;

  private List<RoomSourceJson> rooms;
}
