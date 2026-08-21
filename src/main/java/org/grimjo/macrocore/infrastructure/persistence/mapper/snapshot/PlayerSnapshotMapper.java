package org.grimjo.macrocore.infrastructure.persistence.mapper.snapshot;

import org.grimjo.macrocore.game.domain.actor.NpcStatus;
import org.grimjo.macrocore.game.domain.actor.Player;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.game.domain.world.Zone;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.PlayerSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.NpcStateJson;

public class PlayerSnapshotMapper {

  public PlayerSnapshotEntity toEntity(Player domain, Long snapshotId) {
    return PlayerSnapshotEntity.builder()
        .snapshotId(snapshotId)
        .playerId(domain.getId().getValue())
        .zoneId(domain.getZoneId().getValue())
        .roomId(domain.getRoomId().getValue())
        .isDead(domain.isDead())
        .state(toJsonState(domain))
        .build();
  }

  public Player toDomain(PlayerSnapshotEntity entity) {
    NpcStateJson state = entity.getState();

    return Player.builder()
        .id(Player.PlayerId.of(entity.getPlayerId()))
        .roomId(Room.RoomId.of(entity.getRoomId()))
        .zoneId(Zone.ZoneId.of(entity.getZoneId()))
        .health(state.getHealth())
        .hunger(state.getHunger())
        .status(entity.isDead() ? NpcStatus.DEAD : NpcStatus.ALIVE)
        .build();
  }

  private NpcStateJson toJsonState(Player domain) {
    return NpcStateJson.builder()
        .health(domain.getHealth())
        .hunger(domain.getHunger())
        .build();
  }
}
