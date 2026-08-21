package org.grimjo.macrocore.infrastructure.persistence.entity.statics;

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

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SettlementEntity.SettlementStaticId.class)
@Table(name = SettlementEntity.TABLE_NAME, schema = GAME_SCHEMA)
public class SettlementEntity {
  public static final String TABLE_NAME = "settlement";

  @Id
  private Long zoneId;
  @Id private Long settlementId;

  private String name;
  private String description;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SettlementStaticId implements Serializable {
    private Long zoneId;
    private Long settlementId;
  }
}
