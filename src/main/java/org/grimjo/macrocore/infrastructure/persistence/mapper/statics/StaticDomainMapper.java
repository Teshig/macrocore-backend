package org.grimjo.macrocore.infrastructure.persistence.mapper.statics;

import org.grimjo.macrocore.game.domain.actor.Player;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.PlayerEntity;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.game.domain.world.Zone;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.NpcEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.RoomEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.SettlementEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.ZoneEntity;

@RequiredArgsConstructor
public class StaticDomainMapper {
  private final ZoneStaticMapper zoneMapper;
  private final RoomStaticMapper roomMapper;
  private final NpcStaticMapper npcMapper;
  private final SettlementStaticMapper settlementMapper;
  private final PlayerStaticMapper playerMapper;

  public Zone toDomain(ZoneEntity entity) { return zoneMapper.toDomain(entity); }
  public Room toDomain(RoomEntity entity) { return roomMapper.toDomain(entity); }
  public NpcBase toDomain(NpcEntity entity) { return npcMapper.toDomain(entity); }
  public SmallSettlement toDomain(SettlementEntity entity) { return settlementMapper.toDomain(entity); }
  public Player toDomain(PlayerEntity entity) { return playerMapper.toDomain(entity); }
}
