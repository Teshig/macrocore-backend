package org.grimjo.macrocore.infrastructure.state;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;
import org.grimjo.macrocore.game.model.global.WorldState;

@Slf4j
@RequiredArgsConstructor
public class InMemoryStateHolder {
  AtomicReference<WorldState> currentState = new AtomicReference<>();

  @PostConstruct
  public void loadWorld() {
    log.info("Loading world state from DB...");

    List<NpcBase> initialPopulation =
        IntStream.range(0, 3)
            .mapToObj(i -> NpcBase.builder().id((long) i).hunger(0).build())
            .toList();

    WorldState initialState =
        WorldState.builder()
            .tick(0L)
            .settlements(
                Map.of(
                    0L,
                    SmallSettlement.builder().settlementId(0L).settlers(initialPopulation).build()))
            .build();
    currentState.set(initialState);
  }

  public WorldState get() {
    return currentState.get();
  }

  public void update(WorldState newState) {
    currentState.set(newState);
  }
}
