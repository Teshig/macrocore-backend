package org.grimjo.macrocore.game.domain.settlement;

import lombok.Builder;
import lombok.Value;

import org.grimjo.macrocore.game.domain.settlement.SmallSettlement.SettlementId;

@Value
@Builder
public class SettlementTransaction {
  SettlementId settlementId;
  String orderId;
  long foodAdded;
}