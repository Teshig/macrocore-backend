package org.grimjo.macrocore.infrastructure.persistence.repository.snapshot;

import java.util.List;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.SettlementSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.SettlementSnapshotEntity.SettlementSnapshotId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementSnapshotRepository extends JpaRepository<SettlementSnapshotEntity, SettlementSnapshotId> {
  List<SettlementSnapshotEntity> findAllBySnapshotId(Long snapshotId);
}
