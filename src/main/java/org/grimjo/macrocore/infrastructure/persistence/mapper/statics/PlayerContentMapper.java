package org.grimjo.macrocore.infrastructure.persistence.mapper.statics;

import org.grimjo.macrocore.infrastructure.persistence.entity.statics.PlayerEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.source.PlayerSourceJson;

public class PlayerContentMapper {

  public PlayerEntity toEntity(PlayerSourceJson source) {
    return PlayerEntity.builder()
        .playerId(source.getId())
        .zoneId(source.getZoneId())
        .roomId(source.getRoomId())
        .name(source.getName())
        .description(source.getDescription())
        .build();
  }
}
