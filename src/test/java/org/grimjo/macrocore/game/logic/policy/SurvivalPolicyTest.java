package org.grimjo.macrocore.game.logic.policy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.politic.Decree;
import org.grimjo.macrocore.game.model.politic.DecreeType;
import org.grimjo.macrocore.game.processor.SettlementProcessingContext;
import org.junit.jupiter.api.Test;

class SurvivalPolicyTest {
  private final SurvivalPolicy policy = SurvivalPolicy.builder().build();

  @Test
  void issueGatherFoodDecree_whenFoodIsCritical() {
    // GIVEN
    var hungryPop = List.of(NpcBase.builder().build(), NpcBase.builder().build());

    var context = SettlementProcessingContext.builder()
        .foodStock(1L)
        .population(hungryPop)
        .decrees(Collections.emptyList())
        .build();

    // WHEN
    var decrees = policy.evaluate(context);

    // THEN
    assertThat(decrees)
        .hasSize(1)
        .first()
        .extracting(Decree::getType)
        .isEqualTo(DecreeType.FOOD_SUPPLY);
  }

  @Test
  void doNothing_whenFoodIsEnough() {
    // GIVEN
    var pop = List.of(NpcBase.builder().build());
    var context = SettlementProcessingContext.builder()
        .foodStock(100L)
        .population(pop)
        .build();

    // WHEN
    var decrees = policy.evaluate(context);

    // THEN
    assertThat(decrees).isEmpty();
  }
}
