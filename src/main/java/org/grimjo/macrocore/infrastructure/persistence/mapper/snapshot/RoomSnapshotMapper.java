package org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot;

import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.RoomSnapshotEntity;
import org.grimjo.macrocore.game.domain.world.Zone;

import org.grimjo.macrocore.infrastructure.persistence.json.RoomStateJson;

public class RoomSnapshotMapper {
  public RoomSnapshotEntity toEntity(Room domain, Long snapshotId) {
    return RoomSnapshotEntity.builder()
        .snapshotId(snapshotId)
        .roomId(domain.getId().getValue())
        .zoneId(domain.getZoneId().getValue())
        .state(toJsonState(domain))
        .build();
  }

  public Room toDomain(RoomSnapshotEntity entity) {
    return Room.builder()
        .id(Room.RoomId.of(entity.getRoomId()))
        .zoneId(Zone.ZoneId.of(entity.getZoneId()))
        .build();
  }

  private RoomStateJson toJsonState(Room domain) {
    return RoomStateJson.builder()
        .build();
  }
}
