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
import org.grimjo.macrocore.game.domain.world.Direction;
import org.grimjo.macrocore.infrastructure.persistence.json.ExitTargetJson;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RoomEntity.RoomStaticId.class)
@Table(name = RoomEntity.TABLE_NAME, schema = GAME_SCHEMA)
public class RoomEntity {
  public static final String TABLE_NAME = "room";

  @Id private Long zoneId;
  @Id private Long roomId;

  private String name;
  private String description;

  @JdbcTypeCode(SqlTypes.JSON)
  private Map<Direction, ExitTargetJson> exits;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RoomStaticId implements Serializable {
    private Long zoneId;
    private Long roomId;
  }
}
