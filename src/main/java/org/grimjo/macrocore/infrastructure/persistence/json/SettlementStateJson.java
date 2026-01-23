package org.grimjo.macrocore.infrastructure.persistence.json;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementStateJson {
  private Map<String, Integer> resources;
  private List<String> buildings;
  private List<String> activePolicies;
}
