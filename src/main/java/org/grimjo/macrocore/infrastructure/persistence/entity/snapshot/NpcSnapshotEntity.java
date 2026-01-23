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
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.NpcSnapshotEntity.NpcSnapshotEntityId;
import org.grimjo.macrocore.infrastructure.persistence.json.NpcStateJson;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(NpcSnapshotEntityId.class)
@Table(name = NpcSnapshotEntity.TABLE_NAME, schema = GAME_SCHEMA)
public class NpcSnapshotEntity {
  public static final String TABLE_NAME = "npc_snapshot";

  @Id private Long snapshotId;
  @Id private String npcId;

  private String zoneId;
  private String roomId;
  private String settlementId;
  private boolean isDead;

  @JdbcTypeCode(SqlTypes.JSON)
  private NpcStateJson state;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class NpcSnapshotEntityId implements Serializable {
    private Long snapshotId;
    private String npcId;
  }
}
