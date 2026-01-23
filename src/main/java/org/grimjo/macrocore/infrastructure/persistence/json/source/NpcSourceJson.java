package org.grimjo.macrocore.infrastructure.persistence.json.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NpcSourceJson {
  private Long id;
  private String name;
  private String description;
  private Long defaultRoomId;
}
