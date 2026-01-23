package org.grimjo.macrocore.infrastructure.persistence.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorldGlobalStateJson {
  private String season;
  private String weather;
}
