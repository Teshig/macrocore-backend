package org.grimjo.macrocore.infrastructure.persistence.mapper.statics;

import org.grimjo.macrocore.game.domain.world.Zone;
import org.grimjo.macrocore.game.domain.world.Zone.ZoneId;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.ZoneEntity;

public class ZoneStaticMapper {
  public Zone toDomain(ZoneEntity entity) {
    return Zone.builder()
        .id(ZoneId.of(entity.getId()))
        .name(entity.getName())
        .description(entity.getDescription())
        .build();
  }
}
