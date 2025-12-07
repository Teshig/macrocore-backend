package org.grimjo.macrocore.game.model;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class SmallSettlement implements Settlement {
  Long settlementId;
  @Builder.Default
  Long foodStock = 0L;
  @Builder.Default
  Long foodRequirements = 0L;
  @Singular
  List<Decree> decrees;

  @Override
  public Settlement updateDecrees(List<Decree> newDecrees) {
    return this.toBuilder()
        .clearDecrees()
        .decrees(newDecrees)
        .build();
  }
}

