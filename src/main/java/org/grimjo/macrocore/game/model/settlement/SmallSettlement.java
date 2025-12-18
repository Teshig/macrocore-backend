package org.grimjo.macrocore.game.model.settlement;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.grimjo.macrocore.game.model.order.SimpleOrder;
import org.grimjo.macrocore.game.model.politic.Decree;

@Value
@Builder(toBuilder = true)
public class SmallSettlement {
  long id;
  String regionId;

  long foodStock;
  long foodRequirements;

  @Singular List<Decree> decrees;
  @Singular List<SimpleOrder> orders;
}
