package org.grimjo.macrocore.infrastructure.persistence.repository.statics;

import java.util.List;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, String> {

  List<RoomEntity> findAllByZoneId(Long zoneId);
}
