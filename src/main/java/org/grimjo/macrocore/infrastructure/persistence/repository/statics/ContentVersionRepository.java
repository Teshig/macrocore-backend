package org.grimjo.macrocore.infrastructure.persistence.repository.statics;

import org.grimjo.macrocore.infrastructure.persistence.entity.statics.ContentVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentVersionRepository extends JpaRepository<ContentVersionEntity, String> {}
