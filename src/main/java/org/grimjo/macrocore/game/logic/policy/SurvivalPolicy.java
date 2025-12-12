package org.grimjo.macrocore.game.logic.policy;

import static java.util.Collections.emptyList;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.processor.SettlementProcessingContext;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.politic.Decree;
import org.grimjo.macrocore.game.model.politic.Policy;
import org.grimjo.macrocore.game.model.politic.SimpleDecree;
import org.grimjo.macrocore.game.model.politic.DecreeType;

@RequiredArgsConstructor
public class SurvivalPolicy implements Policy {

  @Override
  public List<Decree> evaluate(SettlementProcessingContext context) {
    var foodStock = context.getFoodStock();
    var foodRequirements = calculateTotalConsumption(context.getPopulation());

    if (foodStock < foodRequirements) {
      return List.of(SimpleDecree.builder().type(DecreeType.FOOD_SUPPLY).priority(100).build());
    }
    return emptyList();
  }

  private long calculateTotalConsumption(List<NpcBase> population) {
    return population.stream().mapToLong(NpcBase::getConsumption).sum();
  }
}
