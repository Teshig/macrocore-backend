package org.grimjo.macrocore.infrastructure.state.genesis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.global.WorldState;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement.SettlementId;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;
import org.grimjo.macrocore.game.domain.world.Zone;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.ZoneEntity;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.StaticDomainMapper;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.NpcRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.RoomRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.SettlementRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.ZoneRepository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Builder
@RequiredArgsConstructor
public class GenesisService {
  private final ZoneRepository zoneRepository;
  private final RoomRepository roomRepository;
  private final NpcRepository npcRepository;
  private final SettlementRepository settlementRepository;

  private final StaticDomainMapper domainMapper;

  @Transactional(readOnly = true)
  public WorldState createInitialWorldState() {
    log.info("Initializing WorldState from static database...");

    Map<Long, Zone> worldZones = new HashMap<>();
    Map<RoomId, Room> worldRooms = new HashMap<>();
    Map<NpcId, NpcBase> worldPopulation = new HashMap<>();
    Map<SettlementId, SmallSettlement> worldSettlements = new HashMap<>();

    List<ZoneEntity> allZones = zoneRepository.findAll();
    log.info("Genesis: Found {} zones to process.", allZones.size());

    for (ZoneEntity zoneEntity : allZones) {
      processZone(
          zoneEntity,
          worldZones,
          worldRooms,
          worldPopulation,
          worldSettlements
      );
    }

    log.info("Genesis: Initialization Complete. Rooms: {}, NPCs: {}, Settlements: {}",
        worldRooms.size(), worldPopulation.size(), worldSettlements.size());

    return WorldState.builder()
        .tick(0L)
        .rooms(worldRooms)
        .population(worldPopulation)
        .settlements(worldSettlements)
        .build();
  }

  private void processZone(
      ZoneEntity zoneEntity,
      Map<Long, Zone> zonesMap,
      Map<RoomId, Room> roomsMap,
      Map<NpcId, NpcBase> npcMap,
      Map<SettlementId, SmallSettlement> settlementsMap
  ) {
    Long zoneId = zoneEntity.getId();
    log.debug("Processing Zone ID: {} ({})", zoneId, zoneEntity.getName());

    zonesMap.put(zoneId, domainMapper.toDomain(zoneEntity));

    roomRepository.findAllByZoneId(zoneId).forEach(entity -> {
      Room room = domainMapper.toDomain(entity);
      roomsMap.put(RoomId.of(zoneId +  room.getId().getValue()), room);
    });

    npcRepository.findAllByZoneId(zoneId).forEach(entity -> {
      NpcBase npc = domainMapper.toDomain(entity);
      npcMap.put(NpcId.of(zoneId + npc.getId().getValue()), npc);
    });

    settlementRepository.findAllByZoneId(zoneId).forEach(entity -> {
      SmallSettlement settlement = domainMapper.toDomain(entity);
      settlementsMap.put(SettlementId.of(zoneId + settlement.getId().getValue()), settlement);
    });
  }
}
