package org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.global.WorldState;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.NpcSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.OrderSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.RoomSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.SettlementSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.WorldSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.ZoneSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.WorldGlobalStateJson;

@RequiredArgsConstructor
public class WorldSnapshotMapper {
  private final NpcSnapshotMapper npcMapper;
  private final RoomSnapshotMapper roomMapper;
  private final SettlementSnapshotMapper settlementMapper;
  private final OrderSnapshotMapper orderMapper;
  private final ZoneSnapshotMapper zoneMapper;

  public WorldGlobalStateJson extractGlobalState(WorldState state) {
    return WorldGlobalStateJson.builder()
        .build();
  }


  public List<NpcSnapshotEntity> toNpcEntities(WorldState state, Long snapshotId) {
    return state.getPopulation().values().stream()
        .map(npc -> npcMapper.toEntity(npc, snapshotId))
        .collect(Collectors.toList());
  }

  public List<RoomSnapshotEntity> toRoomEntities(WorldState state, Long snapshotId) {
    return state.getRooms().values().stream()
        .map(room -> roomMapper.toEntity(room, snapshotId))
        .collect(Collectors.toList());
  }


  public List<OrderSnapshotEntity> toOrderEntities(WorldState state, Long snapshotId) {
    return List.of();
  }

  public WorldState assembleWorldState(
      WorldSnapshotEntity header,
      List<NpcSnapshotEntity> npcs,
      List<RoomSnapshotEntity> rooms,
      List<SettlementSnapshotEntity> settlements,
      List<OrderSnapshotEntity> orders,
      List<ZoneSnapshotEntity> zones
  ) {
    return WorldState.builder()
        .tick(header.getTick())
        .population(mapNpcs(npcs))
        .rooms(mapRooms(rooms))
        // .settlements(...)
        // .zones(...)
        .build();
  }

  private Map<NpcId, NpcBase> mapNpcs(List<NpcSnapshotEntity> entities) {
    return entities.stream()
        .map(npcMapper::toDomain)
        .collect(Collectors.toMap(NpcBase::getId, npc -> npc));
  }

  private Map<Room.RoomId, Room> mapRooms(List<RoomSnapshotEntity> entities) {
    return entities.stream()
        .map(roomMapper::toDomain)
        .collect(Collectors.toMap(Room::getId, room -> room));
  }
}
