package org.grimjo.macrocore.game.logic.mechanic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.grimjo.macrocore.game.model.politic.DecreeType;
import org.grimjo.macrocore.game.model.politic.Policy;
import org.grimjo.macrocore.game.model.politic.SimpleDecree;
import org.grimjo.macrocore.game.processor.SettlementProcessingContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TownAssemblyServiceTest {
  @Mock
  private Policy survivalPolicy;
  @Mock
  private Policy anotherPolicy;

  private final TownAssemblyService service = TownAssemblyService.builder().build();

  @Test
  void holdMeeting_ShouldReturnDecreesFromAllPolicies() {
    // GIVEN
    var context = SettlementProcessingContext.builder().build();
    var decreeA = SimpleDecree.builder().type(DecreeType.FOOD_SUPPLY).build();
    var decreeB = SimpleDecree.builder().type(DecreeType.FOOD_SUPPLY).build();

    when(survivalPolicy.evaluate(any(SettlementProcessingContext.class)))
        .thenReturn(List.of(decreeA));
    when(anotherPolicy.evaluate(any(SettlementProcessingContext.class)))
        .thenReturn(List.of(decreeB));

    List<Policy> activePolicies = List.of(survivalPolicy, anotherPolicy);

    // WHEN
    var resultDecrees = service.holdMeeting(context, activePolicies);

    // THEN
    assertThat(resultDecrees)
        .hasSize(2)
        .containsExactlyInAnyOrder(decreeA, decreeB);
  }

  @Test
  void holdMeeting_ShouldReturnEmptyListWhenNoPoliciesAreActive() {
    // GIVEN
    var context = SettlementProcessingContext.builder().build();

    // WHEN
    var resultDecrees = service.holdMeeting(context, Collections.emptyList());

    // THEN
    assertThat(resultDecrees).isEmpty();
  }
}
