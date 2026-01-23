package org.grimjo.macrocore.infrastructure.persistence.entity.statics;

import static org.grimjo.macrocore.infrastructure.persistence.DbConstant.GAME_SCHEMA;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(NpcEntity.NpcStaticId.class)
@Table(name = NpcEntity.TABLE_NAME, schema = GAME_SCHEMA)
public class NpcEntity {
  public static final String TABLE_NAME = "npc";

  @Id private Long zoneId;
  @Id private Long npcId;

  private String name;
  private String description;

  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> baseStats;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class NpcStaticId implements Serializable {
    private Long zoneId;
    private Long npcId;
  }
}
