package org.grimjo.macrocore.game.processor.settlement;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.grimjo.macrocore.game.domain.politic.Decree;
import org.grimjo.macrocore.game.domain.settlement.SettlementTransaction;

@Value
@Builder
public class SettlementProcessingContext {
  long foodStock;

  @Singular
  List<Decree> decrees;

  @Builder.Default
  List<SettlementTransaction> transactions = List.of();
}
