package org.grimjo.macrocore.game.model.politic;

import java.util.List;
import org.grimjo.macrocore.game.processor.SettlementProcessingContext;

public interface Policy {
  List<Decree> evaluate(SettlementProcessingContext context);
}
