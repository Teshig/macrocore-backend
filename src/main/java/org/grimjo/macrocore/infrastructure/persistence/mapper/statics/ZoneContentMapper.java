package org.grimjo.macrocore.infrastructure.persistence.mapper.statics;

import org.grimjo.macrocore.infrastructure.persistence.entity.statics.ZoneEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.source.ZoneSourceJson;

public class ZoneContentMapper {
  public ZoneEntity toEntity(ZoneSourceJson source) {
    return ZoneEntity.builder()
        .id(source.getId())
        .name(source.getName())
        .description(source.getDescription())
        .build();
  }
}
