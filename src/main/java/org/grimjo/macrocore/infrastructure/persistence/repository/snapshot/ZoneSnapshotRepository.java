package org.grimjo.macrocore.infrastructure.persistence.repository.snapshot;

import java.util.List;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.ZoneSnapshotEntity;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.ZoneSnapshotEntity.ZoneSnapshotId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneSnapshotRepository extends JpaRepository<ZoneSnapshotEntity, ZoneSnapshotId> {
  List<ZoneSnapshotEntity> findAllBySnapshotId(Long snapshotId);
}
