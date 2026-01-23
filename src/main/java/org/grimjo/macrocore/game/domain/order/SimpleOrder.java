package org.grimjo.macrocore.game.domain.order;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SimpleOrder {
  @Builder.Default String id = UUID.randomUUID().toString();

  OrderType type;
  int priority;
  String description;
  Long assignedNpcId;
  @Builder.Default OrderStatus status = OrderStatus.TODO;
}
