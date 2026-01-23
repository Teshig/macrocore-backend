package org.grimjo.macrocore.game.logic.policy;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.domain.politic.Decree;
import org.grimjo.macrocore.game.domain.politic.DecreeType;
import org.grimjo.macrocore.game.domain.politic.Policy;
import org.grimjo.macrocore.game.domain.politic.SimpleDecree;
import org.grimjo.macrocore.game.processor.settlement.SettlementProcessingContext;

@Builder
@RequiredArgsConstructor
public class SurvivalPolicy implements Policy {
  private static final long FOOD_THRESHOLD = 100L;

  @Override
  public List<Decree> evaluate(SettlementProcessingContext context) {
    long currentStock = context.getFoodStock();

    if (currentStock < FOOD_THRESHOLD) {
      return List.of(
          SimpleDecree.builder()
              .type(DecreeType.FOOD_SUPPLY)
              .parameters(Collections.emptyMap())
              .build()
      );
    }

    return Collections.emptyList();
  }
}
