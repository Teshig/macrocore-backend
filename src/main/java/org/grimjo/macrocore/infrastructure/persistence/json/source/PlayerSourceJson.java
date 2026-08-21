package org.grimjo.macrocore.infrastructure.persistence.json.source;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerSourceJson {
  private Long id;
  private Long zoneId;
  private Long roomId;
  private String name;
  private String description;
}
