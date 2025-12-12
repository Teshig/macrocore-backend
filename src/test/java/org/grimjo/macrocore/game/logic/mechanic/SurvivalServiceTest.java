package org.grimjo.macrocore.game.logic.mechanic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.actor.NpcStatus;
import org.junit.jupiter.api.Test;

class SurvivalServiceTest {
  private final SurvivalService service = new SurvivalService();

  @Test
  void shouldFeedEveryone() {
    // GIVEN
    long foodStock = 100L;
    var peasant = NpcBase.builder().id(1L).hunger(50).build();
    List<NpcBase> population = List.of(peasant);

    // WHEN
    var result = service.processDailySurvival(population, foodStock);

    // THEN
    assertThat(result.remainingFood()).isEqualTo(99L);

    assertThat(result.survivors())
        .hasSize(1)
        .first()
        .satisfies(
            p -> {
              assertThat(p.getId()).isEqualTo(1L);
              assertThat(p.getHunger()).isEqualTo(0); // Стал сыт
            });
  }

  @Test
  void shouldStarvePopulation() {
    // GIVEN
    long foodStock = 0L;
    var peasant = NpcBase.builder().id(1L).hunger(10).build();
    List<NpcBase> population = List.of(peasant);

    // WHEN
    var result = service.processDailySurvival(population, foodStock);

    // THEN
    assertThat(result.remainingFood()).isEqualTo(0L);
    assertThat(result.survivors()).hasSize(1).first().extracting(NpcBase::getHunger).isEqualTo(20);
  }

  @Test
  void shouldKillStarvingNpc() {
    // GIVEN
    long foodStock = 0L;
    var dyingPeasant = NpcBase.builder().id(1L).hunger(95).build();
    List<NpcBase> population = List.of(dyingPeasant);

    // WHEN
    var result = service.processDailySurvival(population, foodStock);

    // THEN
    assertThat(result.survivors())
        .hasSize(1)
        .first()
        .extracting(NpcBase::getStatus)
        .isEqualTo(NpcStatus.DEAD);
  }
}
