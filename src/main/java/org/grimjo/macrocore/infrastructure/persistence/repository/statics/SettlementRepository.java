package org.grimjo.macrocore.infrastructure.persistence.repository.statics;

import java.util.List;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<SettlementEntity, String> {

  List<SettlementEntity> findAllByZoneId(Long zoneId);
}
