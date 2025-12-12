package org.grimjo.macrocore.game.model.settlement;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.grimjo.macrocore.game.model.politic.Decree;
import org.grimjo.macrocore.game.model.actor.NpcBase;

@Value
@Builder(toBuilder = true)
public class SmallSettlement implements Settlement {
  Long settlementId;

  long foodStock;
  long foodRequirements;

  @Singular List<NpcBase> settlers;
  @Singular List<Decree> decrees;

  @Override
  public Settlement updateDecrees(List<Decree> newDecrees) {
    return this.toBuilder().clearDecrees().decrees(newDecrees).build();
  }

  @Override
  public long getFoodRequirements() {
    return Optional.ofNullable(settlers)
        .orElse(Collections.emptyList())
        .stream()
        .map(NpcBase::getConsumption)
        .reduce(0L, Long::sum);
  }

  public SmallSettlement updatePopulation(List<NpcBase> newSettlers) {
    return this.toBuilder()
        .clearSettlers()
        .settlers(newSettlers)
        .build();
  }
}
