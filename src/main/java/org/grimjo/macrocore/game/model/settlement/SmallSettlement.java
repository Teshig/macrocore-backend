package org.grimjo.macrocore.game.model.settlement;

import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import org.grimjo.macrocore.game.model.order.SimpleOrder;
import org.grimjo.macrocore.game.model.politic.Decree;
import org.grimjo.macrocore.game.model.politic.Policy;

@Value
@Builder(toBuilder = true)
public class SmallSettlement {
  String id;
  String regionId;

  long foodStock;
  long foodRequirements;

  @Default List<Decree> decrees = List.of();
  @Default List<SimpleOrder> orders = List.of();
  @Default List<Policy> policies = List.of();
}
