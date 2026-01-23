package org.grimjo.macrocore.infrastructure.persistence.entity.statics;

import static org.grimjo.macrocore.infrastructure.persistence.DbConstant.GAME_SCHEMA;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = ContentVersionEntity.TABLE_NAME, schema = GAME_SCHEMA)
public class ContentVersionEntity {
  public static final String TABLE_NAME = "content_version";

  @Id
  private String fileName;

  private String contentHash;
  private LocalDateTime updatedAt;
}
