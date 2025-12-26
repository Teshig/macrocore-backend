package org.grimjo.macrocore.game.model.settlement;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SettlementTransaction {
  String settlementId;
  String orderId;
  long foodAdded;
}