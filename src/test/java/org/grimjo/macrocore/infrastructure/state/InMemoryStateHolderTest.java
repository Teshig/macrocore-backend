package org.grimjo.macrocore.infrastructure.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.grimjo.macrocore.game.model.global.WorldState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryStateHolderTest {

  private InMemoryStateHolder stateHolder;

  @BeforeEach
  void setUp() {
    stateHolder = InMemoryStateHolder.builder().build();
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
    assertThat(initialState.getSettlements().get(0L).getSettlers()).hasSize(3);
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
    assertThat(updatedState.getTick()).isEqualTo(10L);
    assertThat(updatedState.getSettlements()).isEmpty();
  }
}
