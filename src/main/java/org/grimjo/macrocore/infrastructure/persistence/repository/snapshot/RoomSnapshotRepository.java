package org.grimjo.macrocore.infrastructure.persistence.repository.snapshot;

import java.util.List;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.RoomSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.RoomSnapshotEntity.RoomSnapshotId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomSnapshotRepository extends JpaRepository<RoomSnapshotEntity, RoomSnapshotId> {
  List<RoomSnapshotEntity> findAllBySnapshotId(Long snapshotId);
}
