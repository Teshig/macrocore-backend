package org.grimjo.macrocore.infrastructure.persistence.repository.snapshot;

import java.util.List;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.PlayerSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerSnapshotRepository extends JpaRepository<PlayerSnapshotEntity, PlayerSnapshotEntity.PlayerSnapshotEntityId> {
  List<PlayerSnapshotEntity> findAllBySnapshotId(Long snapshotId);
}
