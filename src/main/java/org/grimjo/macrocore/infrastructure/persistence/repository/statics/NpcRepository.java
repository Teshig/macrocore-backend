package org.grimjo.macrocore.infrastructure.persistence.repository.statics;

import java.util.List;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.NpcEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NpcRepository extends JpaRepository<NpcEntity, String> {

  List<NpcEntity> findAllByZoneId(Long zoneId);
}
