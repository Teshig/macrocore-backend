package org.grimjo.macrocore.infrastructure.persistence.entity.statics;

import static org.grimjo.macrocore.infrastructure.persistence.DbConstant.GAME_SCHEMA;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = PlayerEntity.TABLE_NAME, schema = GAME_SCHEMA)
public class PlayerEntity {
  public static final String TABLE_NAME = "player";

  @Id private Long playerId;
  
  private Long zoneId;
  private Long roomId;

  private String name;
  private String description;
}
