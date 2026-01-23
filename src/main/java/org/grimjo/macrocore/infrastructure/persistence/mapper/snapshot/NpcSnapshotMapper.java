package org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot;

import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcStatus;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.NpcSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.NpcStateJson;

public class NpcSnapshotMapper {

  public NpcSnapshotEntity toEntity(NpcBase domain, Long snapshotId) {
    return NpcSnapshotEntity.builder()
        .snapshotId(snapshotId)
        .npcId(String.valueOf(domain.getId().getValue()))
        .zoneId(domain.getZoneId().getValue())
        .roomId(domain.getRoomId().getValue())
        .settlementId(domain.getSettlementId())
        .isDead(domain.isDead())
        .state(toJsonState(domain))
        .build();
  }

  public NpcBase toDomain(NpcSnapshotEntity entity) {
    NpcStateJson state = entity.getState();

    return NpcBase.builder()
        .id(NpcBase.NpcId.of(entity.getNpcId()))
        .roomId(Room.RoomId.of(entity.getRoomId()))
        .settlementId(entity.getSettlementId())
        .health(state.getHealth())
        .hunger(state.getHunger())
        .energy(state.getEnergy())
        .status(entity.isDead() ? NpcStatus.DEAD : NpcStatus.ALIVE)
        .build();
  }

  private NpcStateJson toJsonState(NpcBase domain) {
    return NpcStateJson.builder()
        .health(domain.getHealth())
        .hunger(domain.getHunger())
        .build();
  }
}
