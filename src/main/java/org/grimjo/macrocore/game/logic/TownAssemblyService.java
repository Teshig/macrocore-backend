package org.grimjo.macrocore.game.logic;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.model.Decree;
import org.grimjo.macrocore.game.model.Policy;
import org.grimjo.macrocore.game.model.Settlement;

@Builder
@RequiredArgsConstructor
public class TownAssemblyService {
  private final Map<Long, List<Policy>> settlementPolicies;

  public List<Decree> holdMeeting(Settlement settlement) {
    List<Policy> policies = settlementPolicies.get(settlement.getSettlementId());

    return policies == null
        ? emptyList()
        : policies.stream().flatMap(policy -> policy.evaluate(settlement).stream()).toList();
  }
}
