package org.grimjo.macrocore.infrastructure.persistence.json;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomStateJson {
  private List<ItemOnFloorJson> items;
  private List<String> corpseIds;
  private List<String> blockedExits;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ItemOnFloorJson {
    private String itemId;
    private int count;
  }
}
