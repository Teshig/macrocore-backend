package org.grimjo.macrocore.infrastructure.persistence.mapper.statics;

import static java.util.Collections.emptyList;

import lombok.Builder;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement.SettlementId;
import org.grimjo.macrocore.game.domain.world.Zone.ZoneId;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.SettlementEntity;

@Builder
public class SettlementStaticMapper {
  public SmallSettlement toDomain(SettlementEntity entity) {
    return SmallSettlement.builder()
        .id(SettlementId.of(entity.getSettlementId()))
        .zoneId(ZoneId.of(entity.getZoneId()))
        .name(entity.getName())
        .description(entity.getDescription())
        .decrees(emptyList())
        .orders(emptyList())
        .policies(emptyList())
        .build();
  }
}
