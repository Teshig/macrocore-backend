package org.grimjo.macrocore.game.domain.settlement;

import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import org.grimjo.macrocore.game.domain.order.SimpleOrder;
import org.grimjo.macrocore.game.domain.politic.Decree;
import org.grimjo.macrocore.game.domain.politic.Policy;
import org.grimjo.macrocore.game.domain.world.Zone.ZoneId;

@Value
@Builder(toBuilder = true)
public class SmallSettlement {
  SettlementId id;
  ZoneId zoneId;

  String name;
  String description;

  long foodStock;
  long foodRequirements;

  @Default List<Decree> decrees = List.of();
  @Default List<SimpleOrder> orders = List.of();
  @Default List<Policy> policies = List.of();

  @Value(staticConstructor = "of")
  public static class SettlementId {
    Long value;
  }
}
