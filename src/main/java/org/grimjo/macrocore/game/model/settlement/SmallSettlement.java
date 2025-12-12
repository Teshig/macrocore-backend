package org.grimjo.macrocore.game.model.settlement;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.politic.Decree;

@Value
@Builder(toBuilder = true)
public class SmallSettlement implements Settlement {
  long id;
  long foodStock;
  long foodRequirements;

  @Singular List<NpcBase> settlers;
  @Singular List<Decree> decrees;
}
