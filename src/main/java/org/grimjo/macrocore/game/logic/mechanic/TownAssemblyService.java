package org.grimjo.macrocore.game.logic.mechanic;

import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.processor.SettlementProcessingContext;
import org.grimjo.macrocore.game.model.politic.Decree;
import org.grimjo.macrocore.game.model.politic.Policy;

@Builder
@RequiredArgsConstructor
public class TownAssemblyService {

  public List<Decree> holdMeeting(SettlementProcessingContext context, List<Policy> policies) {
    return policies.stream().flatMap(policy -> policy.evaluate(context).stream()).toList();
  }
}
