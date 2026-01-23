package org.grimjo.macrocore.infrastructure.persistence.json.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementSourceJson {
  private Long id;
  private String name;
  private String description;
}
