package org.grimjo.macrocore.infrastructure.persistence.mapper.statics;

import org.grimjo.macrocore.infrastructure.persistence.entity.statics.SettlementEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.source.SettlementSourceJson;

public class SettlementContentMapper {
  public SettlementEntity toEntity(SettlementSourceJson source, Long zoneId) {
    return SettlementEntity.builder()
        .zoneId(zoneId)
        .settlementId(source.getId())
        .name(source.getName())
        .description(source.getDescription())
        .build();
  }
}
