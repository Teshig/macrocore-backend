package org.grimjo.macrocore.infrastructure.persistence.mapper.statics;

import lombok.Builder;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.actor.NpcStatus;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;
import org.grimjo.macrocore.game.domain.world.Zone.ZoneId;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.NpcEntity;

@Builder
public class NpcStaticMapper {

  public NpcBase toDomain(NpcEntity entity) {
    return NpcBase.builder()
        .id(NpcId.of(entity.getNpcId()))
        .zoneId(ZoneId.of(entity.getZoneId()))
        .roomId(RoomId.of(1L))
        .settlementId(null)
        .name(entity.getName())
        .description(entity.getDescription())
        .status(NpcStatus.ALIVE)
        .health(100)
        .hunger(0)
        .money(1)
        .build();
  }
}
