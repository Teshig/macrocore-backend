package org.grimjo.macrocore.game.processor;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.politic.Decree;

@Value
@Builder
public class SettlementProcessingContext {
  long foodStock;

  @Singular("npc")
  List<NpcBase> population;

  @Singular
  List<Decree> decrees;
}
