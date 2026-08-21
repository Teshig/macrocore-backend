package org.grimjo.macrocore.infrastructure.persistence.entity.snapshot;

import static org.grimjo.macrocore.infrastructure.persistence.DbConstant.GAME_SCHEMA;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grimjo.macrocore.infrastructure.persistence.json.NpcStateJson;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PlayerSnapshotEntity.PlayerSnapshotEntityId.class)
@Table(name = PlayerSnapshotEntity.TABLE_NAME, schema = GAME_SCHEMA)
public class PlayerSnapshotEntity {
  public static final String TABLE_NAME = "player_snapshot";

  @Id private Long snapshotId;
  @Id private Long playerId;
  private Long zoneId;
  private Long roomId;
  private boolean isDead;

  @JdbcTypeCode(SqlTypes.JSON)
  private NpcStateJson state;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PlayerSnapshotEntityId implements Serializable {
    private Long snapshotId;
    private Long playerId;
  }
}
