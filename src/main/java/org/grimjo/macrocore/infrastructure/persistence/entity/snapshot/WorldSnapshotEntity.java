package org.grimjo.macrocore.infrastructure.persistence.entity.snapshot;

import static org.grimjo.macrocore.infrastructure.persistence.DbConstant.GAME_SCHEMA;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.Data;
import org.grimjo.macrocore.infrastructure.persistence.json.WorldGlobalStateJson;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = WorldSnapshotEntity.TABLE_NAME, schema = GAME_SCHEMA)
public class WorldSnapshotEntity {
  public static final String TABLE_NAME = "world_snapshot";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long tick;
  private Timestamp savedAt;

  @JdbcTypeCode(SqlTypes.JSON)
  private WorldGlobalStateJson globalState;
}