package org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot;

import org.grimjo.macrocore.game.domain.settlement.SmallSettlement;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.SettlementSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.SettlementStateJson;

public class SettlementSnapshotMapper {

  public SettlementSnapshotEntity toEntity(SmallSettlement domain, Long snapshotId) {
    return SettlementSnapshotEntity.builder()
        .snapshotId(snapshotId)
        .settlementId(domain.getId().getValue())
        .zoneId(domain.getZoneId().getValue())
        .state(toJsonState(domain))
        .build();
  }

  public SmallSettlement toDomain(SettlementSnapshotEntity entity) {
    return SmallSettlement.builder()
        .id(SmallSettlement.SettlementId.of(entity.getSettlementId()))
        .zoneId(org.grimjo.macrocore.game.domain.world.Zone.ZoneId.of(entity.getZoneId()))
        .build();
  }

  private SettlementStateJson toJsonState(SmallSettlement domain) {
    return SettlementStateJson.builder()
        .build();
  }
}
