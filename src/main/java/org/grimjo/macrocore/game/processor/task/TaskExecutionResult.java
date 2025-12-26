package org.grimjo.macrocore.game.processor.task;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.model.actor.NpcBase;
import org.grimjo.macrocore.game.model.settlement.SettlementTransaction;

@Value
@Builder
public class TaskExecutionResult {
  Map<String, NpcBase> updatedNpcs;
  @Builder.Default
  List<SettlementTransaction> transactions = List.of();
}