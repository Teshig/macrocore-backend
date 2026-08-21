package org.grimjo.macrocore.infrastructure.persistence.repository.statics;

import org.grimjo.macrocore.infrastructure.persistence.entity.statics.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
}
