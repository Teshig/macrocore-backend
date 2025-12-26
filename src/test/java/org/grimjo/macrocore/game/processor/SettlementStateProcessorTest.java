package org.grimjo.macrocore.game.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.grimjo.macrocore.game.logic.mechanic.OrderService;
import org.grimjo.macrocore.game.logic.mechanic.TownAssemblyService;
import org.grimjo.macrocore.game.model.order.OrderType;
import org.grimjo.macrocore.game.model.order.SimpleOrder;
import org.grimjo.macrocore.game.model.politic.Decree;
import org.grimjo.macrocore.game.model.politic.DecreeType;
import org.grimjo.macrocore.game.model.politic.Policy;
import org.grimjo.macrocore.game.model.politic.SimpleDecree;
import org.grimjo.macrocore.game.model.settlement.SettlementTransaction;
import org.grimjo.macrocore.game.model.settlement.SmallSettlement;
import org.grimjo.macrocore.game.processor.settlement.SettlementStateProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettlementStateProcessorTest {
  @Mock private TownAssemblyService townAssemblyService;
  @Mock private OrderService orderService;

  @InjectMocks private SettlementStateProcessor processor;

  @Test
  void process_shouldApplyTransactions_updateFood_andRefreshOrders() {
    // --- GIVEN ---
    SimpleOrder order1 = SimpleOrder.builder().id("order-1").type(OrderType.COLLECT_FOOD).build();
    SimpleOrder order2 = SimpleOrder.builder().id("order-2").type(OrderType.IDLE).build();

    Policy mockPolicy = context -> Collections.emptyList();

    SmallSettlement settlement = SmallSettlement.builder()
        .id("1L")
        .foodStock(100L)
        .orders(List.of(order1, order2))
        .policies(List.of(mockPolicy))
        .build();

    List<SettlementTransaction> transactions = List.of(
        SettlementTransaction.builder()
            .settlementId("1L")
            .orderId("order-1")
            .foodAdded(50L)
            .build(),
        SettlementTransaction.builder()
            .settlementId("2L")
            .foodAdded(999L)
            .build()
    );

    Decree newDecree = SimpleDecree.builder().type(DecreeType.FOOD_SUPPLY).build();
    when(townAssemblyService.holdMeeting(any(), anyList())).thenReturn(List.of(newDecree));

    SimpleOrder order3 = SimpleOrder.builder().id("order-3").type(OrderType.IDLE).build();
    when(orderService.generateOrders(anyList(), anyList())).thenReturn(List.of(order2, order3));

    // --- WHEN ---
    SmallSettlement result = processor.process(settlement, transactions);

    // --- THEN ---
    assertThat(result.getFoodStock()).isEqualTo(150L);
    assertThat(result.getDecrees()).containsExactly(newDecree);
    assertThat(result.getOrders()).containsExactly(order2, order3);

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<SimpleOrder>> captor = ArgumentCaptor.forClass(List.class);
    verify(orderService).generateOrders(captor.capture(), eq(List.of(newDecree)));

    List<SimpleOrder> capturedOrders = captor.getValue();
    assertThat(capturedOrders).hasSize(1);
    assertThat(capturedOrders.getFirst().getId()).isEqualTo("order-2");
  }
}
