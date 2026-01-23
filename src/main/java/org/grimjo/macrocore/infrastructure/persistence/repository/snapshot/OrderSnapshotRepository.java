package org.grimjo.macrocore.infrastructure.persistence.repository.snapshot;

import java.util.List;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.OrderSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.OrderSnapshotEntity.OrderSnapshotId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSnapshotRepository extends JpaRepository<OrderSnapshotEntity, OrderSnapshotId> {
  List<OrderSnapshotEntity> findAllBySnapshotId(Long snapshotId);
}
