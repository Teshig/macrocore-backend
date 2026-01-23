package org.grimjo.macrocore.infrastructure.persistence.repository.snapshot;

import java.util.Optional;
import org.grimjo.macrocore.infrastructure.persistence.entity.snapshot.WorldSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldSnapshotRepository extends JpaRepository<WorldSnapshotEntity, Long> {
  Optional<WorldSnapshotEntity> findFirstByOrderByTickDesc();
}
