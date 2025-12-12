package org.grimjo.macrocore.game.logic.mechanic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.actor.NpcStatus;
import org.grimjo.macrocore.game.model.item.Corpse;
import org.junit.jupiter.api.Test;

class LifecycleServiceTest {
  private final LifecycleService service = LifecycleService.builder().build();

  @Test
  void processLifecycle_separateAliveFromDead() {
    // GIVEN
    var livingNpc = NpcBase.builder().id(1L).status(NpcStatus.ALIVE).build();
    var dyingNpc = NpcBase.builder().id(2L).status(NpcStatus.DEAD).build();
    var anotherLivingNpc = NpcBase.builder().id(3L).status(NpcStatus.ALIVE).build();

    List<NpcBase> mixedPopulation = List.of(livingNpc, dyingNpc, anotherLivingNpc);

    // WHEN
    var result = service.processLifecycle(mixedPopulation);

    // THEN
    assertThat(result.getSurvivors())
        .hasSize(2)
        .extracting(NpcBase::getId)
        .containsExactlyInAnyOrder(1L, 3L);
    assertThat(result.getCorpses())
        .hasSize(1)
        .first()
        .extracting(Corpse::getOriginalNpcId)
        .isEqualTo(2L);
  }

  @Test
  void processLifecycle_handleEmptyList() {
    // GIVEN
    List<NpcBase> emptyPopulation = Collections.emptyList();

    // WHEN
    var result = service.processLifecycle(emptyPopulation);

    // THEN
    assertThat(result.getSurvivors()).isEmpty();
    assertThat(result.getCorpses()).isEmpty();
  }

  @Test
  void processLifecycle_handleAllAlivePopulation() {
    // GIVEN
    var livingNpc = NpcBase.builder().id(1L).status(NpcStatus.ALIVE).build();
    List<NpcBase> allAlive = List.of(livingNpc);

    // WHEN
    var result = service.processLifecycle(allAlive);

    // THEN
    assertThat(result.getSurvivors()).hasSize(1);
    assertThat(result.getCorpses()).isEmpty();
  }
}
