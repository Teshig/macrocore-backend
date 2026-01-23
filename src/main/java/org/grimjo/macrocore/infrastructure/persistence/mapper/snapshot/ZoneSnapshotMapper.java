package org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot;

import org.grimjo.macrocore.game.domain.world.Zone;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.ZoneSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.source.ZoneSourceJson;

public class ZoneSnapshotMapper {

  public ZoneSnapshotEntity toEntity(Zone domain, Long snapshotId) {
    return ZoneSnapshotEntity.builder()
        .snapshotId(snapshotId)
        .zoneId(domain.getId().getValue())
        .state(toJsonState(domain))
        .build();
  }

  private ZoneSourceJson toJsonState(Zone domain) {
    return ZoneSourceJson.builder()
        .level(domain.getLevel())
        .build();
  }
}
