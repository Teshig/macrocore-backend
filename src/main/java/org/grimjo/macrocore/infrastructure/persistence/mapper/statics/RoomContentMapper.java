package org.grimjo.macrocore.infrastructure.persistence.mapper.statics;

import org.grimjo.macrocore.infrastructure.persistence.entity.statics.RoomEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.source.RoomSourceJson;

public class RoomContentMapper {
  public RoomEntity toEntity(RoomSourceJson source, Long zoneId) {
    return RoomEntity.builder()
        .zoneId(zoneId)
        .roomId(source.getId())
        .name(source.getName())
        .description(source.getDescription())
        .exits(source.getExits())
        .build();
  }
}
