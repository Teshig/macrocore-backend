package org.grimjo.macrocore.infrastructure.api.websocket.dto;

import lombok.Data;

@Data
public class AttackCommandRequest {
    private Long attackerId;
    private Long targetId;
}
