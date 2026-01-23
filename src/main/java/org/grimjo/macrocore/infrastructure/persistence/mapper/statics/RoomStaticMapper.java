package org.grimjo.macrocore.infrastructure.persistence.mapper.statics;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import org.grimjo.macrocore.game.domain.world.Direction;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.game.domain.world.Room.ExitTarget;
import org.grimjo.macrocore.game.domain.world.Zone.ZoneId;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.RoomEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.ExitTargetJson;

@Builder
public class RoomStaticMapper {

  public Room toDomain(RoomEntity entity) {
    return Room.builder()
        .id(Room.RoomId.of(entity.getRoomId()))
        .zoneId(ZoneId.of(entity.getZoneId()))
        .name(entity.getName())
        .description(entity.getDescription())
        .exits(mapExits(entity.getExits()))
        .build();
  }

  private Map<Direction, ExitTarget> mapExits(Map<Direction, ExitTargetJson> exits) {
    if (exits == null) {
      return Map.of();
    }
    return exits.entrySet().stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                entry ->
                    ExitTarget.of(entry.getValue().getRoomId(), entry.getValue().getZoneId())));
  }
}
