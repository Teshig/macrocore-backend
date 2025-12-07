package org.grimjo.macrocore.game.model;

import java.util.List;

public interface Settlement {
  Long getFoodStock();
  Long getFoodRequirements();
  Long getSettlementId();
  List<Decree> getDecrees();
  Settlement updateDecrees(List<Decree> newDecrees);
}
