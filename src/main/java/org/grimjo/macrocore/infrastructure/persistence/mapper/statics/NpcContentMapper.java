package org.grimjo.macrocore.infrastructure.persistence.mapper.statics;

import org.grimjo.macrocore.infrastructure.persistence.entity.statics.NpcEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.source.NpcSourceJson;

public class NpcContentMapper {
  public NpcEntity toEntity(NpcSourceJson source, Long zoneId) {
    return NpcEntity.builder()
        .zoneId(zoneId)
        .npcId(source.getId())
        .name(source.getName())
        .description(source.getDescription())
        .build();
  }
}
