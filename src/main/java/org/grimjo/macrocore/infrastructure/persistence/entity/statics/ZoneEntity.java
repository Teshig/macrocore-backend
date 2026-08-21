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
@Table(name = ZoneEntity.TABLE_NAME, schema = GAME_SCHEMA)
public class ZoneEntity {
  public static final String TABLE_NAME = "zone";

  @Id
  private Long id;

  private String name;
  private String description;
}
