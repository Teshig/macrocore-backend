package org.grimjo.macrocore.game.logic.mechanic;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.grimjo.macrocore.game.processor.settlement.SettlementProcessingContext;
import org.grimjo.macrocore.game.domain.politic.Decree;
import org.grimjo.macrocore.game.domain.politic.Policy;

@Builder
@RequiredArgsConstructor
public class TownAssemblyService {

  public List<Decree> holdMeeting(SettlementProcessingContext context, List<Policy> policies) {
    return policies == null || policies.isEmpty()
        ? List.of()
        : policies.stream()
            .flatMap(policy -> policy.evaluate(context).stream())
            .collect(
                Collectors.toMap(
                    Decree::getType, Function.identity(), (existing, replacement) -> existing))
            .values()
            .stream()
            .toList();
  }
}
