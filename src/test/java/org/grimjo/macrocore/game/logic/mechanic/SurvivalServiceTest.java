package org.grimjo.macrocore.game.logic.mechanic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.actor.NpcStatus;
import org.junit.jupiter.api.Test;

class SurvivalServiceTest {
  private final SurvivalService service = SurvivalService.builder().build();

  @Test
  void feedEveryone() {
    // GIVEN
    long foodStock = 100L;
    var peasant = NpcBase.builder().id(1L).hunger(50).build();
    List<NpcBase> population = List.of(peasant);

    // WHEN
    var result = service.processDailySurvival(population, foodStock);

    // THEN
    assertThat(result.getRemainingFood()).isEqualTo(99L);

    assertThat(result.getSurvivors())
        .hasSize(1)
        .first()
        .satisfies(
            p -> {
              assertThat(p.getId()).isEqualTo(1L);
              assertThat(p.getHunger()).isZero();
            });
  }

  @Test
  void starvePopulation() {
    // GIVEN
    long foodStock = 0L;
    var peasant = NpcBase.builder().id(1L).hunger(10).build();
    List<NpcBase> population = List.of(peasant);

    // WHEN
    var result = service.processDailySurvival(population, foodStock);

    // THEN
    assertThat(result.getRemainingFood()).isZero();
    assertThat(result.getSurvivors())
        .hasSize(1)
        .first()
        .extracting(NpcBase::getHunger)
        .isEqualTo(20);
  }

  @Test
  void killStarvingNpc() {
    // GIVEN
    long foodStock = 0L;
    var dyingPeasant = NpcBase.builder().id(1L).hunger(95).build();
    List<NpcBase> population = List.of(dyingPeasant);

    // WHEN
    var result = service.processDailySurvival(population, foodStock);

    // THEN
    assertThat(result.getSurvivors())
        .hasSize(1)
        .first()
        .extracting(NpcBase::getStatus)
        .isEqualTo(NpcStatus.DEAD);
  }

  @Test
  void processDailySurvival_skipAlreadyDeadNpcs() {
    // GIVEN
    long initialFood = 100L;

    var deadNpc = NpcBase.builder().id(1L).status(NpcStatus.DEAD).hunger(50).build();
    var livingNpc = NpcBase.builder().id(2L).status(NpcStatus.ALIVE).hunger(50).build();
    List<NpcBase> population = List.of(deadNpc, livingNpc);

    // WHEN
    var result = service.processDailySurvival(population, initialFood);

    // THEN
    assertThat(result.getRemainingFood()).isEqualTo(99L);
    assertThat(result.getSurvivors())
        .filteredOn(npc -> npc.getId() == 1L)
        .hasSize(1)
        .first()
        .satisfies(
            npc -> {
              assertThat(npc.isDead()).isTrue();
              assertThat(npc.getHunger()).isEqualTo(50);
            });
  }
}
