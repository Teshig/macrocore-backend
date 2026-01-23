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
import org.grimjo.macrocore.infrastructure.persistence.json.RoomStateJson;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RoomSnapshotEntity.RoomSnapshotId.class)
@Table(name = RoomSnapshotEntity.TABLE_NAME, schema = GAME_SCHEMA)
public class RoomSnapshotEntity {
  public static final String TABLE_NAME = "room_snapshot";

  @Id private Long snapshotId;
  @Id private String roomId;

  private String zoneId;

  @JdbcTypeCode(SqlTypes.JSON)
  private RoomStateJson state;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RoomSnapshotId implements Serializable {
    private Long snapshotId;
    private String roomId;
  }
}
