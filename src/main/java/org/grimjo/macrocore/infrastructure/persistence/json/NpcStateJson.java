package org.grimjo.macrocore.infrastructure.persistence.json;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NpcStateJson {
  private int health;
  private int hunger;
  private int energy;

  private String currentAction;
  private double morale;

  private Map<String, Integer> inventory;

  private Map<String, String> equipment;
}
