package org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.global.WorldState;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.game.domain.actor.Player;
import org.grimjo.macrocore.game.domain.actor.Player.PlayerId;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.NpcSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.OrderSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.PlayerSnapshotEntity;
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
  private final PlayerSnapshotMapper playerMapper;

  public WorldGlobalStateJson extractGlobalState(WorldState state) {
    return WorldGlobalStateJson.builder()
        .build();
  }


  public List<NpcSnapshotEntity> toNpcEntities(WorldState state, Long snapshotId) {
    return state.getPopulation().values().stream()
        .map(npc -> npcMapper.toEntity(npc, snapshotId))
        .collect(Collectors.toList());
  }

  public List<PlayerSnapshotEntity> toPlayerEntities(WorldState state, Long snapshotId) {
    return state.getPlayers().values().stream()
        .map(player -> playerMapper.toEntity(player, snapshotId))
        .collect(Collectors.toList());
  }

  public List<RoomSnapshotEntity> toRoomEntities(WorldState state, Long snapshotId) {
    return state.getRooms().values().stream()
        .map(room -> roomMapper.toEntity(room, snapshotId))
        .collect(Collectors.toList());
  }


  public List<ZoneSnapshotEntity> toZoneEntities(WorldState state, Long snapshotId) {
    return List.of(); // Zones are currently static and don't seem to be part of WorldState
  }

  public List<SettlementSnapshotEntity> toSettlementEntities(WorldState state, Long snapshotId) {
    return state.getSettlements().values().stream()
        .map(settlement -> settlementMapper.toEntity(settlement, snapshotId))
        .collect(Collectors.toList());
  }

  public List<OrderSnapshotEntity> toOrderEntities(WorldState state, Long snapshotId) {
    return List.of();
  }

  public WorldState assembleWorldState(
      WorldSnapshotEntity header,
      List<ZoneSnapshotEntity> zones,
      List<RoomSnapshotEntity> rooms,
      List<NpcSnapshotEntity> npcs,
      List<PlayerSnapshotEntity> players,
      List<SettlementSnapshotEntity> settlements,
      List<OrderSnapshotEntity> orders
  ) {
    return WorldState.builder()
        .tick(header.getTick())
        .population(mapNpcs(npcs))
        .players(mapPlayers(players))
        .rooms(mapRooms(rooms))
        .settlements(mapSettlements(settlements))
        .build();
  }

  private Map<PlayerId, Player> mapPlayers(List<PlayerSnapshotEntity> entities) {
    return entities.stream()
        .map(playerMapper::toDomain)
        .collect(Collectors.toMap(Player::getId, player -> player));
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

  private Map<org.grimjo.macrocore.game.domain.settlement.SmallSettlement.SettlementId, org.grimjo.macrocore.game.domain.settlement.SmallSettlement> mapSettlements(List<SettlementSnapshotEntity> entities) {
    return entities.stream()
        .map(settlementMapper::toDomain)
        .collect(Collectors.toMap(org.grimjo.macrocore.game.domain.settlement.SmallSettlement::getId, settlement -> settlement));
  }
}
