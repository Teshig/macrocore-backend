package org.grimjo.macrocore.infrastructure.state;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.model.SmallSettlement;
import org.grimjo.macrocore.game.model.WorldState;

@Slf4j
@RequiredArgsConstructor
public class InMemoryStateHolder {
  //  private final SettlementRepository settlementRepository;
  AtomicReference<WorldState> currentState = new AtomicReference<>();

  @PostConstruct
  public void loadWorld() {
    log.info("Loading world state from DB...");
    //    var entities = repository.findAll();

    //    Map<Long, Settlement> domainMap = entities.stream()
    //        .map(mapper::toDomain)
    //        .collect(Collectors.toMap(Settlement::getSettlementId, s -> s));
    //
    //    // Инициализируем тик 0 (или берем из БД, если сохраняли)
    //    currentState.set(new WorldState(0, domainMap));
    //    log.info("World loaded. Settlements: {}", domainMap.size());
    WorldState initialState =
        WorldState.builder()
            .tick(0L)
            .settlements(Map.of(0L, SmallSettlement.builder().settlementId(0L).build()))
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
