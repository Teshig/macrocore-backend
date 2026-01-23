package org.grimjo.macrocore.infrastructure.persistence.service;

import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.domain.global.WorldState;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.WorldSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot.WorldSnapshotMapper;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.NpcSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.OrderSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.RoomSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.SettlementSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.WorldSnapshotRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.snapshot.ZoneSnapshotRepository;

@Slf4j
@Builder
@RequiredArgsConstructor
public class SnapshotPersistenceService {
  private final WorldSnapshotRepository worldRepo;
  private final ZoneSnapshotRepository zoneRepo;
  private final RoomSnapshotRepository roomRepo;
  private final NpcSnapshotRepository npcRepo;
  private final SettlementSnapshotRepository settlementRepo;
  private final OrderSnapshotRepository orderRepo;

  private final WorldSnapshotMapper worldSnapshotMapper;

  @Transactional
  public void saveWorldSnapshot(WorldState worldState) {
    long tick = worldState.getTick();
    log.info("Saving snapshot for tick {}...", tick);

    WorldSnapshotEntity header = new WorldSnapshotEntity();
    header.setTick(tick);
    header.setSavedAt(Timestamp.from(Instant.now()));

    header = worldRepo.save(header);
    Long snapshotId = header.getId();

    // 2. Сохраняем компоненты (Batch Inserts)
    // Маппер превращает Domain Objects -> List<Entity>

    zoneRepo.saveAll(worldStateMapper.toZoneEntities(worldState, snapshotId));
    roomRepo.saveAll(worldStateMapper.toRoomEntities(worldState, snapshotId));
    npcRepo.saveAll(worldStateMapper.toNpcEntities(worldState, snapshotId));
    settlementRepo.saveAll(worldStateMapper.toSettlementEntities(worldState, snapshotId));
    orderRepo.saveAll(worldStateMapper.toOrderEntities(worldState, snapshotId));

    log.info("Snapshot #{} (tick {}) saved successfully.", snapshotId, tick);
  }

  /**
   * Загружает последний сохраненный снимок и собирает WorldState.
   */
  @Transactional(readOnly = true)
  public Optional<WorldState> loadLatestSnapshot() {
    log.debug("Looking for latest snapshot...");

    // 1. Ищем последний заголовок
    Optional<WorldSnapshotEntity> headerOpt = worldRepo.findFirstByOrderByTickDesc();
    if (headerOpt.isEmpty()) {
      return Optional.empty();
    }

    WorldSnapshotEntity header = headerOpt.get();
    Long snapshotId = header.getId();
    log.info("Found snapshot #{} (tick {}). Loading components...", snapshotId, header.getTick());

    // 2. Загружаем данные из всех таблиц
    // Благодаря индексам по snapshot_id это будет быстро
    var zones = zoneRepo.findAllBySnapshotId(snapshotId);
    var rooms = roomRepo.findAllBySnapshotId(snapshotId);
    var npcs = npcRepo.findAllBySnapshotId(snapshotId);
    var settlements = settlementRepo.findAllBySnapshotId(snapshotId);
    var orders = orderRepo.findAllBySnapshotId(snapshotId);

    // 3. Собираем WorldState через маппер
    WorldState state = worldStateMapper.assembleWorldState(
        header,
        zones,
        rooms,
        npcs,
        settlements,
        orders
    );

    return Optional.of(state);
  }
}
