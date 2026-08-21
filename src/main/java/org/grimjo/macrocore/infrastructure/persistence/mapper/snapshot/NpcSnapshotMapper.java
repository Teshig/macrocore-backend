package org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot;

import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcStatus;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.game.domain.world.Zone;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement.SettlementId;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.NpcSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.NpcStateJson;

public class NpcSnapshotMapper {

  public NpcSnapshotEntity toEntity(NpcBase domain, Long snapshotId) {
    return NpcSnapshotEntity.builder()
        .snapshotId(snapshotId)
        .npcId(domain.getId().getValue())
        .zoneId(domain.getZoneId().getValue())
        .roomId(domain.getRoomId().getValue())
        .settlementId(domain.getSettlementId() != null ? domain.getSettlementId().getValue() : null)
        .isDead(domain.isDead())
        .state(toJsonState(domain))
        .build();
  }

  public NpcBase toDomain(NpcSnapshotEntity entity) {
    NpcStateJson state = entity.getState();

    return NpcBase.builder()
        .id(NpcBase.NpcId.of(entity.getNpcId()))
        .roomId(Room.RoomId.of(entity.getRoomId()))
        .zoneId(Zone.ZoneId.of(entity.getZoneId()))
        .settlementId(entity.getSettlementId() != null ? SettlementId.of(entity.getSettlementId()) : null)
        .health(state.getHealth())
        .hunger(state.getHunger())
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
