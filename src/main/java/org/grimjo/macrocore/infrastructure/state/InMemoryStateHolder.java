package org.grimjo.macrocore.infrastructure.state;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.actor.NpcStatus;
import org.grimjo.macrocore.game.model.global.WorldState;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;
import org.grimjo.macrocore.game.model.world.RoomId;

@Slf4j
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class InMemoryStateHolder {

  @Builder.Default AtomicReference<WorldState> currentState = new AtomicReference<>();

  @PostConstruct
  public void loadWorld() {
    log.info("Loading world state from DB...");

    Map<String, NpcBase> initialPopulation =
        IntStream.range(0, 3)
            .mapToObj(i -> NpcBase.builder()
                .id(UUID.randomUUID().toString())
                .name("Settler " + i)
                .hunger(0)
                .health(100)
                .status(NpcStatus.ALIVE)
                .roomId(RoomId.of("village_square"))
                .build())
            .collect(Collectors.toMap(
                NpcBase::getId,
                Function.identity()
            ));

    WorldState initialState =
        WorldState.builder()
            .tick(0L)
            .settlements(
                Map.of(0L, SmallSettlement.builder().id(0L).build()))
            .population(initialPopulation)
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
