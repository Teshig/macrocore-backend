package org.grimjo.macrocore.infrastructure.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.grimjo.macrocore.game.logic.policy.SurvivalPolicy;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.global.WorldState;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StateHolderTest {
  @Mock
  private SurvivalPolicy survivalPolicy;

  private StateHolder stateHolder;

  @BeforeEach
  void setUp() {
    stateHolder = StateHolder.builder()
        .build();
  }

  @Test
  void loadWorld_initializeWorldStateWithDefaultValues() {
    // GIVEN
    stateHolder.loadWorld();

    // WHEN
    WorldState initialState = stateHolder.get();

    // THEN
    assertThat(initialState).isNotNull();
    assertThat(initialState.getTick()).isZero();

    assertThat(initialState.getSettlements()).hasSize(1);
    assertThat(initialState.getSettlements()).containsKey("0");

    SmallSettlement settlement = initialState.getSettlements().get("0");
    assertThat(settlement.getPolicies()).contains(survivalPolicy);

    assertThat(initialState.getPopulation()).hasSize(3);

    NpcBase settler = initialState.getPopulation().values().iterator().next();
    assertThat(settler.getHunger()).isZero();
    assertThat(settler.getHealth()).isEqualTo(100);
    assertThat(settler.getRoomId().getValue()).isEqualTo("village_square");
    assertThat(settler.getSettlementId()).isEqualTo("0");
  }

  @Test
  void get_returnCurrentWorldState() {
    // GIVEN
    stateHolder.loadWorld();
    WorldState expectedState = stateHolder.get();

    // WHEN
    WorldState actualState = stateHolder.get();

    // THEN
    assertThat(actualState).isSameAs(expectedState);
  }

  @Test
  void update_setNewWorldState() {
    // GIVEN
    stateHolder.loadWorld();

    WorldState newState = WorldState.builder()
        .tick(10L)
        .settlements(Collections.emptyMap())
        .build();

    // WHEN
    stateHolder.update(newState);
    WorldState updatedState = stateHolder.get();

    // THEN
    assertThat(updatedState).isSameAs(newState);
    assertThat(updatedState.getTick()).isEqualTo(10L);
  }
}
