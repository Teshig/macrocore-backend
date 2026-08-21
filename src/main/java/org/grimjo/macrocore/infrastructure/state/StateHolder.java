package org.grimjo.macrocore.infrastructure.state;

import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.domain.global.WorldState;
import org.grimjo.macrocore.infrastructure.persistence.service.SnapshotPersistenceService;
import org.grimjo.macrocore.infrastructure.state.genesis.GenesisService;

import org.grimjo.macrocore.infrastructure.state.partition.PartitionedStateRegistry;

@Slf4j
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class StateHolder {

  private final GenesisService genesisService;
  private final SnapshotPersistenceService persistenceService;
  private final PartitionedStateRegistry partitionedStateRegistry;

  @Builder.Default AtomicReference<WorldState> currentState = new AtomicReference<>();

  @PostConstruct
  public void loadWorld() {
    Optional<WorldState> savedState = persistenceService.loadLatestSnapshot();

    if (savedState.isPresent()) {
      log.info("Loaded existing world state at tick {}", savedState.get().getTick());
      currentState.set(savedState.get());
      partitionedStateRegistry.initializeFromWorldState(savedState.get());
    } else {
      log.info("No snapshots found in DB. Triggering Genesis...");
      WorldState initialState = genesisService.createInitialWorldState();

      persistenceService.saveWorldSnapshot(initialState);

      currentState.set(initialState);
      partitionedStateRegistry.initializeFromWorldState(initialState);
      log.info("Genesis world state initialized and saved as tick 0");
    }
  }

  public WorldState get() {
    return currentState.get();
  }

  public void update(WorldState newState) {
    currentState.set(newState);
  }
}
