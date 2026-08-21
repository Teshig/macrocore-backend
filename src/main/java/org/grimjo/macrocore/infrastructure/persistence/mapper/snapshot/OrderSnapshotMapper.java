package org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot;

import org.grimjo.macrocore.game.domain.order.SimpleOrder;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.OrderSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.OrderStateJson;

public class OrderSnapshotMapper {

  public OrderSnapshotEntity toEntity(SimpleOrder domain, Long settlementId, Long snapshotId) {
    return OrderSnapshotEntity.builder()
        .snapshotId(snapshotId)
        .settlementId(settlementId)
        .orderId(domain.getId())
        .state(toJsonState(domain))
        .build();
  }

  private OrderStateJson toJsonState(SimpleOrder domain) {
    return OrderStateJson.builder()
        .type(domain.getType().name())
        .status(domain.getStatus().name())
        .build();
  }
}
