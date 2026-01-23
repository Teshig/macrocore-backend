package org.grimjo.macrocore.infrastructure.persistence.repository.statics;

import org.grimjo.macrocore.infrastructure.persistence.entity.statics.ZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<ZoneEntity, String> {
}
