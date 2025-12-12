package org.grimjo.macrocore.game.model.settlement;

import java.util.List;
import org.grimjo.macrocore.game.model.politic.Decree;
import org.grimjo.macrocore.game.model.actor.NpcBase;

public sealed interface Settlement permits SmallSettlement {
  long getId();

  long getFoodStock();

  long getFoodRequirements();

  List<NpcBase> getSettlers();

  List<Decree> getDecrees();
}
