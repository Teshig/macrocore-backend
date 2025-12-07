package org.grimjo.macrocore.game.logic;

import static java.util.Collections.emptyList;

import java.util.List;
import org.grimjo.macrocore.game.model.Decree;
import org.grimjo.macrocore.game.model.Policy;
import org.grimjo.macrocore.game.model.Settlement;
import org.grimjo.macrocore.game.model.SimpleDecree;
import org.grimjo.macrocore.game.model.DecreeType;

public class SurvivalPolicy implements Policy {

  @Override
  public List<Decree> evaluate(Settlement settlement) {
    Long foodStock = settlement.getFoodStock();
    Long foodRequirements = settlement.getFoodRequirements();
    if (foodStock < foodRequirements) {
      return List.of(SimpleDecree.builder().type(DecreeType.FOOD_SUPPLY).priority(100).build());
    }
    return emptyList();
  }
}
