package org.grimjo.macrocore.infrastructure.persistence.mapper.statics;

import lombok.Builder;
import org.grimjo.macrocore.game.domain.actor.Player;
import org.grimjo.macrocore.game.domain.actor.Player.PlayerId;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;
import org.grimjo.macrocore.game.domain.world.Zone.ZoneId;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.PlayerEntity;

@Builder
public class PlayerStaticMapper {

  public Player toDomain(PlayerEntity entity) {
    return Player.builder()
        .id(PlayerId.of(entity.getPlayerId()))
        .zoneId(ZoneId.of(entity.getZoneId()))
        .roomId(RoomId.of(entity.getRoomId()))
        .name(entity.getName())
        .description(entity.getDescription())
        .health(100)
        .hunger(0)
        .money(0)
        .build();
  }
}
