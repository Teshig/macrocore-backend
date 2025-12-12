package org.grimjo.macrocore.game.model.settlement;

import java.util.List;
import org.grimjo.macrocore.game.model.politic.Decree;
import org.grimjo.macrocore.game.model.actor.NpcBase;

public sealed interface Settlement permits SmallSettlement {
  Long getSettlementId();

  long getFoodStock();
  List<NpcBase> getSettlers();

  long getFoodRequirements();

  List<Decree> getDecrees();

  Settlement updateDecrees(List<Decree> newDecrees);

  SmallSettlement updatePopulation(List<NpcBase> settlers);
}
