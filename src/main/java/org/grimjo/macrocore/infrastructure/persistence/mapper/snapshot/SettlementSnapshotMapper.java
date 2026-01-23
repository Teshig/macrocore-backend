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

  private SettlementStateJson toJsonState(SmallSettlement domain) {
    return SettlementStateJson.builder()
        .build();
  }
}
