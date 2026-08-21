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
import org.grimjo.macrocore.infrastructure.persistence.json.source.ZoneSourceJson;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ZoneSnapshotEntity.ZoneSnapshotId.class)
@Table(name = ZoneSnapshotEntity.TABLE_NAME, schema = GAME_SCHEMA)
public class ZoneSnapshotEntity {
  public static final String TABLE_NAME = "zone_snapshot";

  @Id private Long snapshotId;
  @Id private Long zoneId;

  @JdbcTypeCode(SqlTypes.JSON)
  private ZoneSourceJson state;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ZoneSnapshotId implements Serializable {
    private Long snapshotId;
    private Long zoneId;
  }
}
