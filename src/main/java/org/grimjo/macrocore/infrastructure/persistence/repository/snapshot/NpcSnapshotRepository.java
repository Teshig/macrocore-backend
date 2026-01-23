package org.grimjo.macrocore.infrastructure.persistence.repository.snapshot;

import java.util.List;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.NpcSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NpcSnapshotRepository
    extends JpaRepository<NpcSnapshotEntity, NpcSnapshotEntity.NpcSnapshotEntityId> {
  List<NpcSnapshotEntity> findAllBySnapshotId(Long snapshotId);
}
