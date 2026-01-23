package org.grimjo.macrocore.game.processor.task;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.settlement.SettlementTransaction;

@Value
@Builder
public class TaskExecutionResult {
  Map<NpcId, NpcBase> updatedNpcs;
  @Builder.Default
  List<SettlementTransaction> transactions = List.of();
}