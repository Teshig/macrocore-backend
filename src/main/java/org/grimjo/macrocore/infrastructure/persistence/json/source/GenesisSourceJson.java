package org.grimjo.macrocore.infrastructure.persistence.json.source;

import java.util.List;
import lombok.Data;

@Data
public class GenesisSourceJson {
  private List<String> activeZones;
}
